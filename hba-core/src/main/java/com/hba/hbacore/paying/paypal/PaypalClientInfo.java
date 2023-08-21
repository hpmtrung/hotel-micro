package com.hba.hbacore.paying.paypal;

public class PaypalClientInfo {
  private String clientId;
  private String clientSecret;
  private String cancelUrl;
  private String returnUrl;

  public String getCancelUrl() {
    return cancelUrl;
  }

  public void setCancelUrl(final String cancelUrl) {
    this.cancelUrl = cancelUrl;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(final String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}