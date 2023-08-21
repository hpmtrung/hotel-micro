package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemTotal {
  private String currencyCode;
  private String value;

  @JsonProperty("currency_code")
  public String getCurrencyCode() {
    return this.currencyCode;
  }

  public ItemTotal setCurrencyCode(final String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

  @JsonProperty("value")
  public String getValue() {
    return this.value;
  }

  public ItemTotal setValue(final String value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    return "ItemTotal{"
        + "currency_code='"
        + currencyCode
        + '\''
        + ", value='"
        + value
        + '\''
        + '}';
  }
}