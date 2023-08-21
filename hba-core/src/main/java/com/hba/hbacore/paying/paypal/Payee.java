package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Payee {
  private String emailAddress;
  private String merchantId;

  @JsonProperty("email_address")
  public String getEmailAddress() {
    return this.emailAddress;
  }

  public Payee setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  @JsonProperty("merchant_id")
  public String getMerchantId() {
    return this.merchantId;
  }

  public Payee setMerchantId(final String merchantId) {
    this.merchantId = merchantId;
    return this;
  }

  @Override
  public String toString() {
    return "Payee{"
        + "emailAddress='"
        + emailAddress
        + '\''
        + ", merchantId='"
        + merchantId
        + '\''
        + '}';
  }
}