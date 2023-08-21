package com.hba.common.saga;

public class ApiReplyFaultException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ApiReplyFaultException(String message) {
    super(message);
  }

  public ApiReplyFaultException(String message, Throwable cause) {
    super(message, cause);
  }

}