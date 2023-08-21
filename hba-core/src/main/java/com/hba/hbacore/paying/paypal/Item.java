package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

  private String name;
  private String description;
  private String quantity;
  private UnitAmount unitAmount;

  @JsonProperty("name")
  public String getName() {
    return this.name;
  }

  public Item setName(final String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  public Item setDescription(final String description) {
    this.description = description;
    return this;
  }

  @JsonProperty("quantity")
  public String getQuantity() {
    return this.quantity;
  }

  public Item setQuantity(final String quantity) {
    this.quantity = quantity;
    return this;
  }

  @JsonProperty("unit_amount")
  public UnitAmount getUnitAmount() {
    return this.unitAmount;
  }

  public Item setUnitAmount(final UnitAmount unitAmount) {
    this.unitAmount = unitAmount;
    return this;
  }

  @Override
  public String toString() {
    return "AppliedSuiteDto{"
        + "name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", quantity='"
        + quantity
        + '\''
        + ", unit_amount="
        + unitAmount
        + '}';
  }
}