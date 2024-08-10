package Application

import Schemas.{EnrichedEvent, InputCSVSchema}
import Utilities.KafkaUtils
import com.twitter.chill.protobuf.ProtobufSerializer
import grpc.modules.{Batch, Event}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

import java.util.Properties

object ValidationTestDataIngestion {

  @throws[Exception]
  def main(args: Array[String]): Unit = {

    val kafkaIP: String = "localhost"
//    val kafkaIP: String = "83.212.109.198"
    //val kafkaIP: String = "node01"

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.getConfig.registerTypeWithKryoSerializer(classOf[Batch], classOf[ProtobufSerializer]) //Register our protobuf using this command


    val kafkaProducerConfigProperties: Properties = KafkaUtils.ConfigKafkaProducer()
    val sourceData: DataStream[String] = env.readTextFile(args(0)).setParallelism(1)
                                            .uid("Ingestion Source")


    /**
      Read stream from source as Strings.
      Filters:
        - the comments starting with #
        - the header columns when it starts with ID,SecType
        - Empty lines
     */
    val initStream: DataStream[InputCSVSchema] = sourceData.filter(x => !x.startsWith("#") && !x.startsWith("ID,SecType") && !x.isEmpty).map(input => {

      if (input.startsWith("-1")){
        InputCSVSchema("-1","-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1","-1",
          "-1","-1", "-1","-1","-1", "-1", "-1", "-1","-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1")
      }else{
        val columns = input.split(",", -1) // We need that otherwise it ignores empty cells and cannot execute
        InputCSVSchema(columns(0), columns(1), columns(2), columns(3), columns(4), columns(5), columns(6), columns(7), columns(8),
          columns(9), columns(10), columns(11), columns(12), columns(13), columns(14), columns(15), columns(16), columns(17),
          columns(18), columns(19), columns(20), columns(21), columns(22), columns(23), columns(24), columns(25), columns(26),
          columns(27), columns(28), columns(29), columns(30), columns(31), columns(32), columns(33), columns(34), columns(35),
          columns(36), columns(37), columns(38))
      }
    }).setParallelism(1).uid("Ingestion Init Filter")

    /**
     * Convert Entries to Events
     * Filter:
     * - Empty Last Trade Price
     * - Empty Trading Time
     */
    val eventStream: DataStream[Event] = initStream.filter(x => !x.Last.isEmpty && !x.Trading_Time.isEmpty)
                                                    .map(new CsvToEventConverter)
                                                    .setParallelism(1)
                                                    .uid("Ingest Entries To Event")

    /**
     * Convert Events to Batches. Add to all events countEntry field and set to 1, so that keyBy leads all the entries to the same operator
     */
    val eventToBatchConverter: DataStream[Batch] = eventStream.filter(_.getLastTradePrice != 0.0)
                                                              .map(y => EnrichedEvent(y,1))
                                                              .keyBy("CountEntry")
                                                              .flatMap(new EventToBatch(batchSize = 10000, lookupSize = 1000, throttler = 1500))
                                                              .uid("Ingestion Event To Batch")


    /**
     * Kafka sink to Topic: topic. Comment out in testing mode
     */
    eventToBatchConverter.sinkTo(KafkaUtils.KafkaSerialSinkGenerator(kafkaIP = kafkaIP,
                                                                      props = kafkaProducerConfigProperties))
                          .uid("Ingestion Sink Serial")
                          .setParallelism(1)

    eventToBatchConverter.sinkTo(KafkaUtils.KafkaParallelSinkGenerator(kafkaIP = kafkaIP,
                                                                        props = kafkaProducerConfigProperties))
                         .uid("Ingestion Sink Parallel")

    env.execute("DataIngestionCSV Job")
  }
}
