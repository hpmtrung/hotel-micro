package com.hba.roomservice.config;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import com.hba.common.saga.SagaCommandDispatcherFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class SagaConfiguration {

  @Bean
  public SagaCommandDispatcherFactory sagaCommandDispatcherFactory(
      KafkaTemplate<String, Message> kafkaTemplate,
      ConcurrentKafkaListenerContainerFactory<String, CommandMessage<? extends Command>>
          containerFactory) {
    return new SagaCommandDispatcherFactory(kafkaTemplate, containerFactory);
  }
}