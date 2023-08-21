package com.hba.common.saga;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.JacksonUtil;
import com.hba.common.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;

@Slf4j
public class SagaCommandDispatcher implements MessageListener<String, CommandMessage<Command>> {

  private final SagaCommandHandlers handlers;
  private final KafkaTemplate<String, Message> kafkaTemplate;

  public SagaCommandDispatcher(
      SagaCommandHandlers handlers, KafkaTemplate<String, Message> kafkaTemplate) {
    this.handlers = handlers;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void onMessage(ConsumerRecord<String, CommandMessage<Command>> record) {
    CommandMessage<Command> commandMessage = record.value();
    if (commandMessage != null) {
      Message message = handlers.handleCommand(commandMessage);
      message.setId(commandMessage.getId());
      if (message.getPayload() != null) {
        message.setPayload(JacksonUtil.serialize(message.getPayload()));
      }
      log.info("Dispatching command {} and message {}", commandMessage, message);
      kafkaTemplate.send(commandMessage.getProducerInChannel(), message);
    }
  }
}