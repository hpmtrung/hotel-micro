package com.hba.common.saga;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.Message;
import com.hba.common.saga.SagaDefinition.StepDefinition;
import com.mongodb.client.result.DeleteResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class SagaManager {

  private final KafkaTemplate<String, CommandMessage<? extends Command>> kafkaTemplate;
  private final MongoTemplate mongoTemplate;
  private final ConcurrentKafkaListenerContainerFactory<String, Message> containerFactory;
  private final Map<String, SagaMessageListener<?>> registeredSagas = new HashMap<>();

  public SagaManager(
      KafkaTemplate<String, CommandMessage<? extends Command>> kafkaTemplate,
      MongoTemplate mongoTemplate,
      ConcurrentKafkaListenerContainerFactory<String, Message> containerFactory) {
    this.kafkaTemplate = kafkaTemplate;
    this.mongoTemplate = mongoTemplate;
    this.containerFactory = containerFactory;
  }

  public <T> T persistSagaInstance(SimpleSaga<T> saga, T state) {
    String id = UUID.randomUUID().toString();
    SagaInstance<T> instance = new SagaInstance<>(id, state, true, 0);

    SagaMessageListener<?> messageListener = registeredSagas.get(saga.getName());
    CountDownLatch latch = new CountDownLatch(1);
    messageListener.latchMap.put(id, latch);

    executeFirstStep(saga, instance);

    boolean intime;
    try {
      intime = latch.await(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      throw new IllegalStateException(
          "CountDownLatch interrupts while processing saga instance id '" + id + "'", e);
    }

    instance = mongoTemplate.findById(id, SagaInstance.class, saga.getMongoCollection());

    if (instance == null) {
      throw new IllegalStateException("Saga instance with id '" + id + "' not found.");
    }

    if (!intime) {
      throw new SagaTimeoutException(saga.getName(), instance.id, instance.state, instance.step);
    }

    DeleteResult result = mongoTemplate.remove(instance, saga.getMongoCollection());
    log.info("Remove saga instance '{}' from mongodb (result: {}).", instance, result);

    if (!instance.success) {
      if (instance.step == -1) {
        throw new SagaStateFailureException(
            saga.getName(), instance.id, instance.state, instance.step);
      } else {
        throw new SagaRollbackFailureException(
            saga.getName(), instance.id, instance.state, instance.step);
      }
    } else {
      if (instance.step != saga.getSagaDefinition().getSteps().size()) {
        log.warn("Clean unsuccessful saga instance");
      }
    }

    return instance.state;
  }

  public void registerSaga(SimpleSaga<?> saga) {
    ConcurrentMessageListenerContainer<String, Message> container =
        containerFactory.createContainer(saga.getInChannel());

    SagaMessageListener<?> messageListener = new SagaMessageListener<>(saga);
    container.getContainerProperties().setMessageListener(messageListener);
    container.getContainerProperties().setGroupId(saga.getName());
    container.getContainerProperties().setSyncCommits(false);
    container.getContainerProperties().setPollTimeout(1000);
    Properties consumerProperties = container.getContainerProperties().getKafkaConsumerProperties();
    consumerProperties.setProperty(
        ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, UUID.randomUUID().toString());
    consumerProperties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "100000");
    container.start();

    registeredSagas.put(saga.getName(), messageListener);
  }

  private <T> void executeFirstStep(SimpleSaga<T> saga, SagaInstance<T> instance) {
    List<StepDefinition<T>> steps = saga.getSagaDefinition().getSteps();
    final int nstep = steps.size();

    while (instance.step < nstep && steps.get(instance.step).getInvoke() == null) {
      instance.step++;
    }
    if (instance.step < nstep) {
      instance = mongoTemplate.save(instance, saga.getMongoCollection());
      doTransaction(steps.get(instance.step).getInvoke(), instance, saga.getInChannel());
    } else {
      throw new IllegalStateException("Saga definition should have at least one invoke step");
    }
  }

  private <T> void doTransaction(
      Function<T, CommandMessage<? extends Command>> transaction,
      SagaInstance<T> instance,
      String producerInChannel) {
    CommandMessage<? extends Command> message =
        CommandMessage.clone(transaction.apply(instance.state));
    message.setId(instance.getId());
    message.setProducerInChannel(producerInChannel);
    kafkaTemplate.send(message.getOutChannel(), message);
  }

  @Getter
  @AllArgsConstructor
  @ToString
  public static class SagaInstance<T> {
    String id;
    T state;
    boolean success;
    int step;
  }

  private class SagaMessageListener<T> implements MessageListener<String, Message> {

    private final SimpleSaga<T> saga;
    private final Map<String, CountDownLatch> latchMap = new HashMap<>();

    private SagaMessageListener(SimpleSaga<T> saga) {
      this.saga = saga;
    }

    public SimpleSaga<T> getSaga() {
      return saga;
    }

    public Map<String, CountDownLatch> getLatchMap() {
      return latchMap;
    }

    @Override
    public void onMessage(ConsumerRecord<String, Message> record) {
      SagaDefinition<T> definition = saga.getSagaDefinition();
      Message message = record.value();
      final String instanceId = message.getId();

      final String collection = saga.getMongoCollection();

      SagaInstance<T> instance = mongoTemplate.findById(instanceId, SagaInstance.class, collection);

      if (instance == null) {
        // throw new IllegalStateException("Instance with id " + instanceId + " not found.");
        log.error("Instance with id '{}' not found.", instanceId);
        return;
      }

      final int nstep = definition.getSteps().size();
      final List<StepDefinition<T>> steps = definition.getSteps();

      final boolean messageSuccess = message.isSuccess();

      // log.info("Processing saga instance {} and message {}", instance, message);

      if (messageSuccess && instance.success) {
        // call current step's invoke if exist
        if (steps.get(instance.step).getReply() != null) {
          try {
            steps.get(instance.step).getReply().accept(instance.getState(), message.getPayload());
          } catch (RuntimeException e) {
            completeSagaInstance(instance);
            throw e;
          }
        }
        instance.step++;
        while (instance.step < nstep && steps.get(instance.step).getInvoke() == null) {
          instance.step++;
        }
        if (instance.step < nstep) {
          doTransaction(steps.get(instance.step).getInvoke(), instance, saga.getInChannel());
          mongoTemplate.save(instance, collection);
        } else {
          log.info("Complete the successfull saga {}", instance);
          mongoTemplate.save(instance, collection);
          completeSagaInstance(instance);
        }
      } else {
        if (!messageSuccess) {
          if (instance.success) {
            instance.success = false;
          } else {
            completeSagaInstance(instance);
            return; // throws SagaRollbackFailureException later
          }
        } else {
          instance.step--;
        }

        while (instance.step >= 0 && steps.get(instance.step).getCompensation() == null) {
          instance.step--;
        }
        if (instance.step >= 0) {
          doTransaction(steps.get(instance.step).getCompensation(), instance, saga.getInChannel());
          mongoTemplate.save(instance, collection);
        } else {
          log.info("Complete rollback the failure saga {}", instance);
          mongoTemplate.save(instance, collection);
          completeSagaInstance(instance);
        }
      }
    }

    private void completeSagaInstance(SagaInstance<T> instance) {
      log.info("Complete saga instance with id '{}' and remove latch", instance.id);
      latchMap.get(instance.id).countDown();
      latchMap.remove(instance.id);
    }
  }
}