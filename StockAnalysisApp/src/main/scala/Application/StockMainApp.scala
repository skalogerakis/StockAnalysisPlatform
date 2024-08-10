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

import Schemas.{CrossoverToReporterSchema, EMASchema, EventUnpackSchema, LastPriceOutputSchema}
import Utilities.{KafkaUtils, ParameterUtils, ReportQ1, ReportQ2}
import com.twitter.chill.protobuf.ProtobufSerializer
import grpc.modules.{Batch, ResultQ1, ResultQ2}
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

import java.util.Properties
import java.util.stream.Collector

object StockMainApp {

  @throws[Exception]
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val kafkaIP: String = "node01"

    val param = ParameterUtils.ParameterParser(args)

    if ( param._3 != -1.0){
      println("Checkpoint Activate")
      env.enableCheckpointing(60000 * param._3)
      env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
      env.getCheckpointConfig.setExternalizedCheckpointCleanup(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
      env.getCheckpointConfig.setCheckpointStorage("file://"+param._4)
    }

    /**
     * Register protobuf serializers/deserializers
     */
    env.getConfig.registerTypeWithKryoSerializer(classOf[Batch], classOf[ProtobufSerializer])
    env.getConfig.registerTypeWithKryoSerializer(classOf[ResultQ1], classOf[ProtobufSerializer])
    env.getConfig.registerTypeWithKryoSerializer(classOf[ResultQ2], classOf[ProtobufSerializer])

    val kafkaProducerConfigProperties: Properties = KafkaUtils.ConfigKafkaProducer()

    /**
     * This implementation works only for serial source
     */
    val sourceKafka: DataStream[Batch] = env.fromSource(KafkaUtils.KafkaSourceGenerator(kafkaIP = kafkaIP),
                                                        WatermarkStrategy.noWatermarks(),
                                                        "Kafka Source")
                                            .name("Kafka Source")
                                            .uid("StockAnalysisApp-Kafka Source")
                                            .setParallelism(1)

    //This is used for the throughput check
    //sourceKafka.flatMap(new ThroughputMeter)

    val eventUnpacked: DataStream[EventUnpackSchema] = sourceKafka.flatMap(new BatchToEventUnpack)
                                                                  .name("BatchToEventUnpack")
                                                                  .uid("StockAnalysisApp-BatchToEventUnpack")
                                                                  .setParallelism(1)

    /**
     * This is the custom operator after data input
     */
    val processedWindow: DataStream[LastPriceOutputSchema] = eventUnpacked.keyBy(_.Symbol)
                                                                          .process(new CustomLastPriceProcess)
                                                                          .name("Custom Window")
                                                                          .uid("StockAnalysisApp-Custom Window")

    /**
     * Calculate the ema for each window
     */
    val emaWin: DataStream[EMASchema] = processedWindow.keyBy(_.Symbol)
                                                        .map(new EMACalculator(j = param._1, w = param._2))
                                                        .name("EMA Calculator")
                                                        .uid("StockAnalysisApp-EMA Calculator")


    // Reporter for Q1
    val q1reporter: DataStream[ResultQ1] = emaWin.keyBy(_.batchID)
                                                  .process(new ReportQ1)
                                                  .name("Q1Reporter")
                                                  .uid("StockAnalysisApp-Q1Reporter")



    q1reporter.sinkTo(KafkaUtils.KafkaValidatorSinkGeneratorQ1(kafkaIP = kafkaIP,
                                                                props = kafkaProducerConfigProperties))
              .name("StockAnalysisApp-Sink Q1")
              .uid("StockAnalysisApp-Sink Q1")

    /**
     * Calculate the crossover events
     */
    val crossoverCalc: DataStream[CrossoverToReporterSchema] = emaWin.keyBy(_.Symbol)
                                                                      .process(new CrossoverCalculator)
                                                                      .name("Crossover Calculator")
                                                                      .uid("StockAnalysisApp-Crossover Calculator")

    //     Reporter for Q2
    val q2reporter: DataStream[ResultQ2] = crossoverCalc.keyBy(_.batchID)
                                                        .process(new ReportQ2)
                                                        .name("Q2Reporter")
                                                        .uid("StockAnalysisApp-Q2Reporter")



    q2reporter.sinkTo(KafkaUtils.KafkaValidatorSinkGeneratorQ2(kafkaIP = kafkaIP,
                                                      props = kafkaProducerConfigProperties))
              .name("Sink Q2")
              .uid("StockAnalysisApp-Sink Q2")

    env.execute("DEBS2022-Challenge")
  }




}
