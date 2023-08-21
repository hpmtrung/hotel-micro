package com.fs.services.exceptions;

public class DataManipulateException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DataManipulateException(String message) {
    super(message);
  }

  public DataManipulateException(String message, Throwable cause) {
    super(message, cause);
  }
}