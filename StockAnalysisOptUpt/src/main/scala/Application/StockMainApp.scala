/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Application

import Schemas._
import Utilities.{KafkaUtils, MainUtils, ReportQ1, ReportQ2, TimestampUtil}
import com.esotericsoftware.kryo.serializers.DefaultSerializers.KryoSerializableSerializer
import com.google.protobuf.util.Timestamps.fromMillis
import com.twitter.chill.protobuf.ProtobufSerializer
import grpc.modules._
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.operators.SlotSharingGroup
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment, createTypeInformation}

import java.util.Properties


object StockMainApp {

  @throws[Exception]
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setMaxParallelism(env.getParallelism * 15)


//    val kafkaIP: String = "localhost"
    val kafkaIP: String = "node01"

    val parallel: Int = env.getParallelism
    val param = MainUtils.parameterParser(args, parallel)

    if (param._3 != -1.0) {
      println("Checkpoint Activate")
      env.enableCheckpointing(60000 * param._3)
      env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
      env.getCheckpointConfig.setExternalizedCheckpointCleanup(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
      env.getCheckpointConfig.setCheckpointStorage("file://" + param._4)
    }

    /**
     * Register protobuf serializers/deserializers
     */

    env.getConfig.registerTypeWithKryoSerializer(classOf[Batch], classOf[ProtobufSerializer])
    env.getConfig.registerTypeWithKryoSerializer(classOf[ResultQ1], classOf[ProtobufSerializer])
    env.getConfig.registerTypeWithKryoSerializer(classOf[ResultQ2], classOf[ProtobufSerializer])


    //TODO must test what is going on here registerTypeWithKryoSerializer or addDefaultKryoSerializer
    env.getConfig.addDefaultKryoSerializer(classOf[EventEnrichmentSchema], classOf[KryoSerializableSerializer])
    env.getConfig.addDefaultKryoSerializer(classOf[EventUnpackSchema], classOf[KryoSerializableSerializer])
    env.getConfig.addDefaultKryoSerializer(classOf[LastPriceOutputSchema], classOf[KryoSerializableSerializer])
    env.getConfig.addDefaultKryoSerializer(classOf[EMASchema], classOf[KryoSerializableSerializer])
    env.getConfig.addDefaultKryoSerializer(classOf[CrossoverToReporterSchema], classOf[KryoSerializableSerializer])



    val kafkaProducerConfigProperties: Properties = KafkaUtils.ConfigKafkaProducer()


    /**
     * Ingest Data from Kafka Source
     */
    val source_kafka: DataStream[Batch] = env.fromSource(KafkaUtils.KafkaSourceGenerator(kafkaIP = kafkaIP), WatermarkStrategy.noWatermarks(), "Kafka Source")
                                              //.slotSharingGroup(ssgA)
                                              .name("Kafka Source")
                                              .uid("StockAnalysisOptUpt-Kafka Source")

      //This is used for the throughput tests
//    source_kafka.flatMap(new ThroughputMeter)

    /**
     * Event Unpack Operator -> Unpack batches to events with metadata including a side-stream indicating the end of a batch
     */
    val event_unpacker: DataStream[EventEnrichmentSchema] = source_kafka.process(new BatchToEventProcess)
                                                                        //.slotSharingGroup(ssgA)
                                                                        .name("BatchToEventUnpack")
                                                                        .uid("StockAnalysisOptUpt-BatchToEventUnpack")

    /**
     * Side-stream with end of batch information
     */
    val compl_batch_side: DataStream[(Long, String)] = event_unpacker.getSideOutput(OutputTag[(Long, String)]("side-output"))

    val broadcastStateDescriptor = new MapStateDescriptor[Long, String]("completedBatchStream", classOf[Long], classOf[String])
    val broadcast_side: BroadcastStream[(Long, String)] = compl_batch_side.broadcast(broadcastStateDescriptor)


    /**
     * EnrichedEvent Operator. Find safe-to-close batches and report to the next operator
     */
    val enrich_event: DataStream[EventUnpackSchema] = event_unpacker.keyBy(_.symbol)
                                                                    .connect(broadcast_side)
                                                                    .process(new ProcessKeyedEnrichedEventTimer(timerTriggerInterval = 250))
                                                                    //.slotSharingGroup(ssgA)
                                                                    .name("EnrichedEvent")
                                                                    .uid("StockAnalysisOptUpt-EnrichedEvent")

    /**
     * Custom Window Operator, responsible to close windows and emit results last price results for EMA
     */
    val process_window: DataStream[LastPriceOutputSchema] = enrich_event.keyBy(_.symbol)
                                                                        .process(new CustomLastPriceProcess)
                                                                        //.slotSharingGroup(ssgB)
                                                                        .name("Custom Window Processing")
                                                                        .uid("StockAnalysisOptUpt-Custom Window Processing")

    /**
     * Calculate the ema for each window
     */
    val ema_calculator: DataStream[EMASchema] = process_window.keyBy(_.symbol)
                                                              .map(new EMACalculator(j = param._1, w = param._2))
                                                              //.slotSharingGroup(ssgB)
                                                              .name("EMA Calculator")
                                                              .uid("StockAnalysisOptUpt-EMA Calculator")


    // Reporter for Q1
    val q1_reporter: DataStream[ResultQ1] = ema_calculator.filter(x => x.is_lookup && x.isLastWindow)
                                                          .keyBy(_.batch_ID)
                                                          .process(new ReportQ1)
                                                          //.slotSharingGroup(ssgB)
                                                          .name("Q1Reporter")
                                                          .uid("StockAnalysisOptUpt-Q1Reporter")

   q1_reporter.sinkTo(KafkaUtils.KafkaSinkGeneratorQ1(kafkaIP = kafkaIP, props = kafkaProducerConfigProperties))
                //.slotSharingGroup(ssgB)
                .name("SinkQ1")
                .uid("StockAnalysisOptUpt-sink1")

    /**
     * Calculate the crossover events
     */
    val crossover_calculator: DataStream[CrossoverToReporterSchema] = ema_calculator.keyBy(_.symbol)
                                                                                    .process(new CrossoverCalculator)
                                                                                    //.slotSharingGroup(ssgB)
                                                                                    .name("CrossoverCalculator")
                                                                                    .uid("StockAnalysisOptUpt-CrossoverCalculator")

    //     Reporter for Q2
    val q2_reporter: DataStream[ResultQ2] = crossover_calculator.keyBy(_.batch_id)
                                                                .process(new ReportQ2)
                                                                //.slotSharingGroup(ssgB)
                                                                .name("Q2Reporter")
                                                                .uid("StockAnalysisOptUpt-Q2Reporter")

   q2_reporter.sinkTo(KafkaUtils.KafkaSinkGeneratorQ2(kafkaIP = kafkaIP, props = kafkaProducerConfigProperties))
                //.slotSharingGroup(ssgB)
                .name("SinkQ2")
                .uid("StockAnalysisOptUpt-sinkQ2")

    env.execute("DEBS2022-Challenge")
  }

}
