package com.fs.sdk;

public class StorageServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String response;

  public StorageServiceException(String response) {
    this.response = response;
  }

  public StorageServiceException(String message, String response) {
    super(message);
    this.response = response;
  }

  public StorageServiceException(String message, Throwable cause) {
    super(message, cause);
    response = null;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + (response != null ? (" [actual_message=" + response + "]") : "");
  }

  public String getResponse() {
    return response;
  }

}