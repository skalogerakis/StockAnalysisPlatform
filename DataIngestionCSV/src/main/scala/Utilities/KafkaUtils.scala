package Utilities

import Serializers.KafkaSerializerTopic
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
//    producer_config.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, "org.apache.kafka.clients.producer.RoundRobinPartitioner")
    producer_config.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, "Utilities.MyPartitioner")
    producer_config
  }


  def KafkaSerialSinkGenerator(kafkaIP: String, props: Properties): KafkaSink[Batch] = {
    KafkaSink
      .builder()
      .setBootstrapServers(s"${kafkaIP}:9092")
      .setKafkaProducerConfig(props)
      .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
      .setTransactionalIdPrefix("Ingestion-Sink-Serial")
      .setRecordSerializer(
        KafkaRecordSerializationSchema.builder()
          .setTopic("topic-serial")
          .setValueSerializationSchema(KafkaSerializerTopic())
          .build()
      )
      .build()

  }

  def KafkaParallelSinkGenerator(kafkaIP: String, props: Properties): KafkaSink[Batch] = {
    KafkaSink
      .builder()
      .setBootstrapServers(s"${kafkaIP}:9092")
      .setKafkaProducerConfig(props)
      .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
      .setTransactionalIdPrefix("Ingestion-Sink-Parallel")
      .setRecordSerializer(
        KafkaRecordSerializationSchema.builder()
          .setTopic("topic-parallel")
          .setValueSerializationSchema(KafkaSerializerTopic())
          .build()
      )
      .build()

  }

}
