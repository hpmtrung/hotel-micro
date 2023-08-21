package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PurchaseUnit {
  private List<Item> items = new ArrayList<>();
  private Amount amount;
  private String description = "";
  private String invoiceId;
  private String referenceId;
  private Payee payee;

  @JsonProperty("payee")
  public Payee getPayee() {
    return payee;
  }

  public PurchaseUnit setPayee(Payee payee) {
    this.payee = payee;
    return this;
  }

  @JsonProperty("reference_id")
  public String getReferenceId() {
    return referenceId;
  }

  public PurchaseUnit setReferenceId(String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  @JsonProperty("invoice_id")
  public String getInvoiceId() {
    return invoiceId;
  }

  public PurchaseUnit setInvoiceId(final String invoiceId) {
    this.invoiceId = invoiceId;
    return this;
  }

  @JsonProperty("items")
  public List<Item> getItems() {
    return this.items;
  }

  public PurchaseUnit setItems(List<Item> items) {
    this.items = items;
    return this;
  }

  @JsonProperty("amount")
  public Amount getAmount() {
    return this.amount;
  }

  public PurchaseUnit setAmount(Amount amount) {
    this.amount = amount;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public PurchaseUnit setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public String toString() {
    return "PurchaseUnit{"
        + "items="
        + items
        + ", amount="
        + amount
        + ", description='"
        + description
        + '\''
        + ", invoiceId='"
        + invoiceId
        + '\''
        + '}';
  }
}