package com.hba.common.saga;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.JacksonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SagaDefinition<T> {

  private final List<StepDefinition<T>> steps;

  public SagaDefinition(Builder<T> builder) {
    this.steps = builder.steps;
  }

  public List<StepDefinition<T>> getSteps() {
    return steps;
  }

  public static class Builder<T> {

    private final List<StepDefinition<T>> steps = new ArrayList<>();

    public Builder<T> onInvoke(Function<T, CommandMessage<? extends Command>> messageSupplier) {
      lastStep().setInvoke(Objects.requireNonNull(messageSupplier));
      return this;
    }

    public Builder<T> onCompensation(
        Function<T, CommandMessage<? extends Command>> messageSupplier) {
      lastStep().setCompensation(Objects.requireNonNull(messageSupplier));
      return this;
    }

    public <P> Builder<T> onReply(Class<P> clazz, BiConsumer<T, P> cs) {
      lastStep().setReply((ConsumerDelegator<T, Object>) new ConsumerDelegator<>(cs, clazz));
      return this;
    }

    public Builder<T> step() {
      this.steps.add(new StepDefinition<>());
      return this;
    }

    public SagaDefinition<T> build() {
      return new SagaDefinition<>(this);
    }

    private StepDefinition<T> lastStep() {
      return this.steps.get(steps.size() - 1);
    }
  }

  public static class StepDefinition<T> {

    private Function<T, CommandMessage<? extends Command>> compensation;
    private Function<T, CommandMessage<? extends Command>> invoke;
    private ConsumerDelegator<T, Object> reply;

    public Function<T, CommandMessage<? extends Command>> getCompensation() {
      return compensation;
    }

    public void setCompensation(Function<T, CommandMessage<? extends Command>> compensation) {
      this.compensation = compensation;
    }

    public Function<T, CommandMessage<? extends Command>> getInvoke() {
      return invoke;
    }

    public void setInvoke(Function<T, CommandMessage<? extends Command>> invoke) {
      this.invoke = invoke;
    }

    public BiConsumer<T, Object> getReply() {
      return reply;
    }

    public void setReply(ConsumerDelegator<T, Object> reply) {
      this.reply = reply;
    }
  }

  public static class ConsumerDelegator<State, Type> implements BiConsumer<State, Type> {
    private final BiConsumer<State, Object> consumer;
    private final Class<Type> resultClass;

    public ConsumerDelegator(BiConsumer<State, Type> consumer, Class<Type> resultClass) {
      this.consumer = (BiConsumer<State, Object>) consumer;
      this.resultClass = resultClass;
    }

    @Override
    public void accept(State state, Type payload) {
      Object ob = JacksonUtil.deserialize((String) payload, resultClass);
      consumer.accept(state, ob);
    }
  }
}