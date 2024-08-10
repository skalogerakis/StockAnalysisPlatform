package Utilities

import Serializers.{KafkaDeserializer, KafkaSerializerQ1, KafkaSerializerQ2}
import grpc.modules.{Batch, ResultQ1, ResultQ2}
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties

object KafkaUtils {

  def ConfigKafkaProducer(): Properties = {
    /**
     * Kafka Producer Properties
     *  - FIXME NOTE -> add in kafka config/server.properties
     *    transaction.max.timeout.ms=90000000
     */

    val producer_config = new Properties()
    producer_config.setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "90000000")
    producer_config.setProperty(ProducerConfig.ACKS_CONFIG, "all")
    producer_config.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
    producer_config.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1")

    producer_config.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "1638400")
    producer_config.setProperty(ProducerConfig.LINGER_MS_CONFIG, "10")
    producer_config
  }


  /**
   * Kafka Source Generator
   */
  def KafkaSourceGenerator(kafkaIP: String): KafkaSource[Batch] = {
    KafkaSource
      .builder()
      .setBootstrapServers(s"${kafkaIP}:9092")
//      .setProperties(props)
      .setTopics("topic-parallel")
      .setGroupId("StockAnalysisOptUpt-events-group")
      .setClientIdPrefix("StockAnalysisOptUpt-prefix-serial-q1")
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(KafkaDeserializer())
      .build()
  }

  /**
   * Kafka Sink Generator
   */
  def KafkaSinkGeneratorQ1(kafkaIP: String,
                           props: Properties): KafkaSink[ResultQ1] = {
    KafkaSink
      .builder()
      .setBootstrapServers(s"${kafkaIP}:9092")
      .setKafkaProducerConfig(props)
      .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
      .setTransactionalIdPrefix("StockAnalysisOptUpt-prefix-Q1")
      .setRecordSerializer(
        KafkaRecordSerializationSchema.builder()
          .setTopic("topicQ1")
          .setValueSerializationSchema(KafkaSerializerQ1())
          .build()
      )
      .build()
  }

  def KafkaSinkGeneratorQ2(kafkaIP: String,
                           props: Properties): KafkaSink[ResultQ2] = {
    KafkaSink
      .builder()
      .setBootstrapServers(s"${kafkaIP}:9092")
      .setKafkaProducerConfig(props)
      .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
      .setTransactionalIdPrefix("StockAnalysisOptUpt-prefix-Q2")
      .setRecordSerializer(
        KafkaRecordSerializationSchema.builder()
          .setTopic("topicQ2")
          .setValueSerializationSchema(KafkaSerializerQ2())
          .build()
      )
      .build()
  }

  /*
    The configs below are for the validator
   */
  def KafkaValidatorSinkGeneratorQ1(kafkaIP: String,
                                    props: Properties): KafkaSink[ResultQ1] = {
        KafkaSink
          .builder()
          .setBootstrapServers(s"${kafkaIP}:9092")
          .setKafkaProducerConfig(props)
          .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
          .setTransactionalIdPrefix("StockAnalysisOptUpt-prefix-Q1")
          .setRecordSerializer(
            KafkaRecordSerializationSchema.builder()
              .setTopic("parallel-topic-q1")
              .setValueSerializationSchema(KafkaSerializerQ1())
              .build()
          )
          .build()
      }

      def KafkaValidatorSinkGeneratorQ2(kafkaIP: String,
                                        props: Properties): KafkaSink[ResultQ2] = {
        KafkaSink
          .builder()
          .setBootstrapServers(s"${kafkaIP}:9092")
          .setKafkaProducerConfig(props)
          .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
          .setTransactionalIdPrefix("StockAnalysisOptUpt-prefix-Q2")
          .setRecordSerializer(
            KafkaRecordSerializationSchema.builder()
              .setTopic("parallel-topic-q2")
              .setValueSerializationSchema(KafkaSerializerQ2())
              .build()
          )
          .build()
      }
}
