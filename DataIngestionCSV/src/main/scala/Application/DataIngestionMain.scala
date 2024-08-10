package Application

import Schemas.{DistrubutionEvent, EnrichedEvent, InputCSVSchema, _EventSchema}
import Serializers.KafkaSerializerTopic
import Utilities.{DistributionUtil, KafkaUtils, TimestampUtil}
import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps
import com.twitter.chill.protobuf.ProtobufSerializer
import grpc.modules.{Batch, Event, SecurityType}
import org.apache.flink.api.common.io.FileInputFormat
import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.kafka.clients.producer.ProducerConfig

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}
import java.util.Properties

object DataIngestionMain {

  @throws[Exception]
  def main(args: Array[String]): Unit = {

    val kafkaIP: String = "localhost"
//    val kafkaIP: String = "node01"

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.getConfig.registerTypeWithKryoSerializer(classOf[Batch], classOf[ProtobufSerializer]) //Register our protobuf using this command


    val kafkaProducerConfigProperties: Properties = KafkaUtils.ConfigKafkaProducer()
    val sourceData: DataStream[String] = env.readTextFile(args(0))
                                            .setParallelism(1)
                                            .uid("Ingestion Source")


    /**
      Read stream from source as Strings.
      Filters:
        - the comments starting with #
        - the header columns when it starts with ID,SecType
        - Empty lines
     */
    val initStream: DataStream[InputCSVSchema] = sourceData.filter(x => !x.startsWith("#") && !x.startsWith("ID,SecType") && !x.isEmpty).map(input => {

      //Todo solution for end of stream. Find something more clever
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
    val eventStream: DataStream[Event] = initStream.filter(x => !x.Last.isEmpty && !x.Trading_Time.isEmpty && x.Trading_Time != "01:00:00.000")
                                                    .map(new CsvToEventConverter)
                                                    .setParallelism(1)
                                                    .uid("Ingest Entries To Event")


    /**
     * Convert Events to Batches. Add to all events countEntry field and set to 1, so that keyBy leads all the entries to the same operator
     */

    val eventToBatchConverter: DataStream[Batch] = eventStream.filter(_.getLastTradePrice != 0.0).map(y => EnrichedEvent(y,1)).keyBy("CountEntry").flatMap(new EventToBatch(batchSize = 10000, lookupSize = 1000, throttler = 15000))

    /**
     * Kafka sink to Topic: topic. Comment out in testing mode
     */
    eventToBatchConverter.sinkTo(KafkaUtils.KafkaSerialSinkGenerator(kafkaIP = kafkaIP,
                                                                      props = kafkaProducerConfigProperties))
                          .name("Ingestion Sink Serial")
                          .uid("Ingestion Sink Serial")
                          .setParallelism(1)

    eventToBatchConverter.sinkTo(KafkaUtils.KafkaParallelSinkGenerator(kafkaIP = kafkaIP,
                                                                        props = kafkaProducerConfigProperties))
                          .name("Ingestion Sink Parallel")
                          .uid("Ingestion Sink Parallel")
                          .setParallelism(1)

    env.execute("DataIngestionCSV Job")
  }
}
