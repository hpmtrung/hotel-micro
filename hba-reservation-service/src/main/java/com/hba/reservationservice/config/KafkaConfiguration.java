package com.hba.reservationservice.config;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.DomainEvent;
import com.hba.common.messaging.Message;
import com.hba.common.messaging.config.CommonKafkaConfiguration;
import com.hba.reservationservice.api.ReservationServiceChannels;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

  @Bean
  public ConsumerFactory<String, Message> consumerFactory() {
    return CommonKafkaConfiguration.messageConsumerFactory();
  }

  @Bean
  public KafkaTemplate<String, CommandMessage<? extends Command>> kafkaTemplate() {
    return CommonKafkaConfiguration.commandKafkaTemplate();
  }

  @Bean
  public KafkaTemplate<Integer, DomainEvent> intKeyKafkaTemplate() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.7:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
  }

  @Bean
  public ProducerFactory<String, CommandMessage<? extends Command>> producerFactory() {
    return CommonKafkaConfiguration.commandProducerFactory();
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
    return CommonKafkaConfiguration.messageKafkaListenerContainerFactory();
  }

  @Bean
  public KafkaAdmin admin() {
    return CommonKafkaConfiguration.defaultKafkaAdmin();
  }

  @Bean
  public NewTopic commandChannel() {
    return TopicBuilder.name(ReservationServiceChannels.RESERVATION_CREATE_COMMAND_CHANNEL)
        .partitions(2)
        .replicas(1)
        .build();
  }
}