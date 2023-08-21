package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationContext {
  String returnUrl;
  String cancelUrl;

  @JsonProperty("return_url")
  public String getReturnUrl() {
    return this.returnUrl;
  }

  public ApplicationContext setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

  @JsonProperty("cancel_url")
  public String getCancelUrl() {
    return this.cancelUrl;
  }

  public ApplicationContext setCancelUrl(String cancelUrl) {
    this.cancelUrl = cancelUrl;
    return this;
  }

  @Override
  public String toString() {
    return "ApplicationContext{"
        + "return_url='"
        + returnUrl
        + '\''
        + ", cancel_url='"
        + cancelUrl
        + '\''
        + '}';
  }
}