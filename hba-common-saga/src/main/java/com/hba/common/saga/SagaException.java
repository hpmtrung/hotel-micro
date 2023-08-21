package com.hba.common.saga;

public class SagaException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String sagaName;
  private final String instanceId;
  private final int step;
  private final Object state;

  public SagaException(String sagaName, String instanceId, int step, Object state) {
    this.sagaName = sagaName;
    this.instanceId = instanceId;
    this.step = step;
    this.state = state;
  }

  public Object getState() {
    return state;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getType() {
    return "failure";
  }

  @Override
  public String getMessage() {
    return String.format(
        "Saga %s exception with instance name=%s, id='%s', step=%d",
        getType(), sagaName, instanceId, step);
  }

  public String getSagaName() {
    return sagaName;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public int getStep() {
    return step;
  }
}