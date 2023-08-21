package com.hba.reservationservice.config;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import com.hba.common.saga.SagaManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class SagaConfiguration {

  @Bean
  public SagaManager sagaManager(
      KafkaTemplate<String, CommandMessage<? extends Command>> kafkaTemplate,
      MongoTemplate mongoTemplate,
      ConcurrentKafkaListenerContainerFactory<String, Message> containerFactory) {
    return new SagaManager(kafkaTemplate, mongoTemplate, containerFactory);
  }
}