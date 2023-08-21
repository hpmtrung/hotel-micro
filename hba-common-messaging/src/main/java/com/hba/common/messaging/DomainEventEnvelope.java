package com.hba.common.messaging;

import java.util.Objects;

public class DomainEventEnvelope<T extends DomainEvent> {
  private String aggregateType;
  private String aggregateId;
  private T event;

  public DomainEventEnvelope() {}

  public DomainEventEnvelope(String aggregateId) {
    this.aggregateId = aggregateId;
  }

  public T getEvent() {
    return event;
  }

  public void setEvent(T event) {
    this.event = event;
  }

  public String getAggregateId() {
    return aggregateId;
  }

  public void setAggregateId(String aggregateId) {
    this.aggregateId = aggregateId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DomainEventEnvelope)) return false;
    DomainEventEnvelope<?> that = (DomainEventEnvelope<?>) o;
    return Objects.equals(aggregateId, that.aggregateId) && Objects.equals(event, that.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aggregateId);
  }

  @Override
  public String toString() {
    return "DomainEventEnvelope{" + "aggregateId='" + aggregateId + '\'' + ", event=" + event + '}';
  }
}