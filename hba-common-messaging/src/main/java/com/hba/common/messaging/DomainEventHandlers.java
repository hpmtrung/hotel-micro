package com.hba.common.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DomainEventHandlers {
  private final String aggregateType;
  private final Map<
          Class<? extends DomainEvent>, Consumer<DomainEventEnvelope<? extends DomainEvent>>>
      eventHandlers;

  public DomainEventHandlers(Builder builder) {
    this.aggregateType = builder.aggregateType;
    this.eventHandlers = builder.eventHandlers;
  }

  public String getAggregateType() {
    return aggregateType;
  }

  public <T extends DomainEvent> void handleEvent(
      DomainEventEnvelope<T> event, Class<T> eventClass) {
    if (eventHandlers.containsKey(eventClass)) {
      eventHandlers.get(eventClass).accept(event);
    }
    throw new HandlerNotFoundException();
  }

  public <T extends DomainEvent>
      Consumer<? extends DomainEventEnvelope<? extends DomainEvent>> getHandler(
          Class<T> eventClass) {
    return eventHandlers.get(eventClass);
  }

  public Builder forAggregateType(String aggregateType) {
    return new Builder(aggregateType);
  }

  public static class Builder {

    private final String aggregateType;
    private final Map<
            Class<? extends DomainEvent>, Consumer<DomainEventEnvelope<? extends DomainEvent>>>
        eventHandlers = new HashMap<>();

    public Builder(String aggregateType) {
      this.aggregateType = aggregateType;
    }

    public <T extends DomainEvent> void onEvent(
        Class<T> eventClass, Consumer<DomainEventEnvelope<?>> handler) {
      eventHandlers.put(eventClass, handler);
    }

    public DomainEventHandlers build() {
      return new DomainEventHandlers(this);
    }
  }
}