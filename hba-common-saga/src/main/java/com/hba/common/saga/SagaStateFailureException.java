package com.hba.common.saga;

public class SagaStateFailureException extends SagaException {

  private static final long serialVersionUID = 1L;

  public SagaStateFailureException(String sagaName, String instanceId, Object state, int step) {
    super(sagaName, instanceId, step, state);
  }

  @Override
  public String getType() {
    return "state failure";
  }

}