package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount {
  private String currencyCode;
  private String value;
  private Breakdown breakdown;

  @JsonProperty("currency_code")
  public String getCurrencyCode() {
    return this.currencyCode;
  }

  public Amount setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

  @JsonProperty("value")
  public String getValue() {
    return this.value;
  }

  public Amount setValue(String value) {
    this.value = value;
    return this;
  }

  @JsonProperty("breakdown")
  public Breakdown getBreakdown() {
    return this.breakdown;
  }

  public Amount setBreakdown(Breakdown breakdown) {
    this.breakdown = breakdown;
    return this;
  }

  @Override
  public String toString() {
    return "Amount{"
        + "currency_code='"
        + currencyCode
        + '\''
        + ", value='"
        + value
        + '\''
        + ", breakdown="
        + breakdown
        + '}';
  }
}