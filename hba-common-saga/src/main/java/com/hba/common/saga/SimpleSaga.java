package com.hba.common.saga;

public interface SimpleSaga<T> {

  SagaDefinition<T> getSagaDefinition();

  String getName();

  String getInChannel();

  default String getMongoCollection() {
    return getName();
  }
}