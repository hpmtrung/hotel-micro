package com.hba.common.saga;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Properties;
import java.util.UUID;

@Slf4j
public class SagaCommandDispatcherFactory {

  private final KafkaTemplate<String, Message> kafkaTemplate;
  private final ConcurrentKafkaListenerContainerFactory<String, CommandMessage<? extends Command>>
      containerFactory;

  public SagaCommandDispatcherFactory(
      KafkaTemplate<String, Message> kafkaTemplate,
      ConcurrentKafkaListenerContainerFactory<String, CommandMessage<? extends Command>>
          containerFactory) {
    this.kafkaTemplate = kafkaTemplate;
    this.containerFactory = containerFactory;
  }

  public void create(String dispatcherId, SagaCommandHandlers handlers) {
    SagaCommandDispatcher dispatcher = new SagaCommandDispatcher(handlers, kafkaTemplate);

    // Create listener container
    ConcurrentMessageListenerContainer<String, CommandMessage<? extends Command>> container =
        containerFactory.createContainer(handlers.getInChannel());
    container.getContainerProperties().setMessageListener(dispatcher);
    container.getContainerProperties().setGroupId(dispatcherId);
    container.getContainerProperties().setSyncCommits(false);
    container.getContainerProperties().setPollTimeout(500);
    container.getContainerProperties().setPollTimeout(1000);

    Properties consumerProperties = container.getContainerProperties().getKafkaConsumerProperties();
    consumerProperties.setProperty(
        ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, UUID.randomUUID().toString());
    consumerProperties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "100000");
    container.start();
  }
}