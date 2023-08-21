package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Breakdown {
  private ItemTotal itemTotal;

  @JsonProperty("item_total")
  public ItemTotal getItemTotal() {
    return this.itemTotal;
  }

  public Breakdown setItemTotal(ItemTotal itemTotal) {
    this.itemTotal = itemTotal;
    return this;
  }

  @Override
  public String toString() {
    return "Breakdown{" + "itemTotal=" + itemTotal + '}';
  }
}