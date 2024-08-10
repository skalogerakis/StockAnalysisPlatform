package Application

import Schemas.{EnrichedEvent, InputCSVSchema}
import Serializers.KafkaSerializerTopic
import com.twitter.chill.protobuf.ProtobufSerializer
import grpc.modules.{Batch, Event}
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties

object DataIngestionDistribution {

  @throws[Exception]
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.getConfig.registerTypeWithKryoSerializer(classOf[Batch], classOf[ProtobufSerializer]) //Register our protobuf using this command

    /**
     * Kafka Producer Properties
     *  - TODO add in kafka config/server.properties
     *    transaction.max.timeout.ms=90000000
     */
    val kafkaProducerConfigProperties = new Properties()

    kafkaProducerConfigProperties.setProperty(
      ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "90000000"
    )
    kafkaProducerConfigProperties.setProperty(
      ProducerConfig.ACKS_CONFIG, "all"
    )
    kafkaProducerConfigProperties.setProperty(
      ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"
    )
    kafkaProducerConfigProperties.setProperty(
      ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1"
    )

    /**
     * Kafka Sinks for both Queries
     */
    val kafkaSinkTopic = KafkaSink.builder()
      .setBootstrapServers("localhost:9092")
      .setKafkaProducerConfig(kafkaProducerConfigProperties)
      .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
      .setTransactionalIdPrefix("Topic-Record-Producer")
      .setRecordSerializer(
        KafkaRecordSerializationSchema.builder()
          .setTopic("topic")
          .setValueSerializationSchema(KafkaSerializerTopic())
          .build()
      )
      .build()


    val sourceData: DataStream[String] = env.readTextFile("/home/skalogerakis/Documents/Workspace/debs2022/DataIngestionCSV/Data/TestData")
    
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
    })

    /**
     * Convert Entries to Events
     * Filter:
     * - Empty Last Trade Price
     * - Empty Trading Time
     */
    val eventStream: DataStream[Event] = initStream.filter(x => !x.Last.isEmpty && !x.Trading_Time.isEmpty)
                                                   .map(new CsvToEventConverter)


    /**
     * Convert Events to Batches. Add to all events countEntry field and set to 1, so that keyBy leads all the entries to the same operator
     */
                                
    val eventToBatchConverter: DataStream[Batch] = eventStream.filter(_.getLastTradePrice != 0.0)
                                                              .map(y => EnrichedEvent(y,1))
                                                              .keyBy("CountEntry")
                                                              .flatMap(new EventToBatch(batchSize = 1000, lookupSize = 5, throttler = 1500))

    /**
     * Kafka sink to Topic: topic. Comment out in testing mode
     */
    eventToBatchConverter.sinkTo(kafkaSinkTopic).uid("Sink Topic")

    env.execute("DataIngestionCSV Job")
  }
}
