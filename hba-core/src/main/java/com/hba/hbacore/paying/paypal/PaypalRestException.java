package com.hba.hbacore.paying.paypal;

public class PaypalRestException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PaypalRestException(String message) {
    super(message);
  }

  public PaypalRestException(String message, Throwable cause) {
    super(message, cause);
  }
}