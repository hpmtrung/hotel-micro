package com.hba.common.messaging.config;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class CommonKafkaConfiguration {

  private CommonKafkaConfiguration() {}

  public static ConsumerFactory<String, CommandMessage<? extends Command>>
      commandConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(CommonKafkaConfiguration.consumerConfigs());
  }

  public static ConsumerFactory<String, Message> messageConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(CommonKafkaConfiguration.consumerConfigs());
  }

  public static KafkaTemplate<String, Message> messageKafkaTemplate() {
    return new KafkaTemplate<>(messageProducerFactory());
  }

  public static KafkaTemplate<String, CommandMessage<? extends Command>> commandKafkaTemplate() {
    return new KafkaTemplate<>(commandProducerFactory());
  }

  public static ConcurrentKafkaListenerContainerFactory<String, CommandMessage<? extends Command>>
      commandKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, CommandMessage<? extends Command>> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(commandConsumerFactory());
    factory.setConcurrency(2);
    factory.getContainerProperties().setPollTimeout(5000);
    factory.getContainerProperties().setSyncCommits(false);
    factory.setCommonErrorHandler(new CommonLoggingErrorHandler());
    return factory;
  }

  public static KafkaAdmin defaultKafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.7:9092");
    return new KafkaAdmin(configs);
  }

  public static Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.7:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, 3_000_000);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 0);
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 100_000);
    return props;
  }

  public static Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.7:9092");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
    props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 300_000);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return props;
  }

  public static ProducerFactory<String, Message> messageProducerFactory() {
    return new DefaultKafkaProducerFactory<>(CommonKafkaConfiguration.producerConfigs());
  }

  public static ProducerFactory<String, CommandMessage<? extends Command>>
      commandProducerFactory() {
    return new DefaultKafkaProducerFactory<>(CommonKafkaConfiguration.producerConfigs());
  }

  public static ConcurrentKafkaListenerContainerFactory<String, Message>
      messageKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Message> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(messageConsumerFactory());
    factory.setConcurrency(2);
    // factory.getContainerProperties().setPollTimeout(5000);
    // factory.getContainerProperties().setSyncCommits(false);
    // factory.setCommonErrorHandler(new CommonLoggingErrorHandler());
    return factory;
  }
}