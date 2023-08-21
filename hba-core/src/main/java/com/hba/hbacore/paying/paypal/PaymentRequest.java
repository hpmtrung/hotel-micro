package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaymentRequest {

  private String intent;
  private List<PurchaseUnit> purchaseUnits;
  private ApplicationContext applicationContext;

  @JsonProperty("intent")
  public String getIntent() {
    return this.intent;
  }

  public PaymentRequest setIntent(final String intent) {
    this.intent = intent;
    return this;
  }

  @JsonProperty("purchase_units")
  public List<PurchaseUnit> getPurchaseUnits() {
    return this.purchaseUnits;
  }

  public PaymentRequest setPurchaseUnits(final List<PurchaseUnit> purchaseUnits) {
    this.purchaseUnits = purchaseUnits;
    return this;
  }

  @JsonProperty("application_context")
  public ApplicationContext getApplicationContext() {
    return this.applicationContext;
  }

  public PaymentRequest setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    return this;
  }

  @Override
  public String toString() {
    return "PaymentRequest{"
        + "intent='"
        + intent
        + '\''
        + ", purchase_units="
        + purchaseUnits
        + ", application_context="
        + applicationContext
        + '}';
  }
}