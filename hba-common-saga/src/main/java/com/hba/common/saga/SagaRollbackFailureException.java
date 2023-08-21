package com.hba.common.saga;

public class SagaRollbackFailureException extends SagaException {

  private static final long serialVersionUID = 1L;

  public SagaRollbackFailureException(String sagaName, String instanceId, Object state, int step) {
    super(sagaName, instanceId, step, state);
  }

  @Override
  public String getType() {
    return "rollback failure";
  }

}