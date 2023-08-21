package com.hba.roomservice.config;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import com.hba.common.messaging.config.CommonKafkaConfiguration;
import com.hba.roomservice.api.RoomServiceChannels;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaConfiguration {
  @Bean
  public KafkaAdmin admin() {
    return CommonKafkaConfiguration.defaultKafkaAdmin();
  }

  @Bean
  public ProducerFactory<String, Message> producerFactory() {
    return CommonKafkaConfiguration.messageProducerFactory();
  }

  @Bean
  public KafkaTemplate<String, Message> kafkaTemplate() {
    return CommonKafkaConfiguration.messageKafkaTemplate();
  }

  @Bean
  public ConsumerFactory<String, CommandMessage<? extends Command>> consumerFactory() {
    return CommonKafkaConfiguration.commandConsumerFactory();
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CommandMessage<? extends Command>>
      kafkaListenerContainerFactory() {
    return CommonKafkaConfiguration.commandKafkaListenerContainerFactory();
  }

  @Bean
  public NewTopic commandChannel() {
    return TopicBuilder.name(RoomServiceChannels.COMMAND_CHANNEL).partitions(2).replicas(1).build();
  }
}