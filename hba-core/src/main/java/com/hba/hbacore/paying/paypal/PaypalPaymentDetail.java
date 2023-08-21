package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hba.hbacore.paying.PaymentDetail;
import org.springframework.hateoas.Link;

import java.util.Date;
import java.util.List;

public class PaypalPaymentDetail implements PaymentDetail {
  private String id;
  private String intent;
  private String status;
  private List<PurchaseUnit> purchaseUnits;
  private Payer payer;
  private Date createTime;
  private List<Link> links;

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  public PaypalPaymentDetail setId(final String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("intent")
  public String getIntent() {
    return this.intent;
  }

  public PaypalPaymentDetail setIntent(final String intent) {
    this.intent = intent;
    return this;
  }

  @JsonProperty("status")
  public String getStatus() {
    return this.status;
  }

  public PaypalPaymentDetail setStatus(final String status) {
    this.status = status;
    return this;
  }

  @JsonProperty("purchase_units")
  public List<PurchaseUnit> getPurchaseUnits() {
    return this.purchaseUnits;
  }

  public PaypalPaymentDetail setPurchaseUnits(final List<PurchaseUnit> purchaseUnits) {
    this.purchaseUnits = purchaseUnits;
    return this;
  }

  @JsonProperty("payer")
  public Payer getPayer() {
    return this.payer;
  }

  public PaypalPaymentDetail setPayer(final Payer payer) {
    this.payer = payer;
    return this;
  }

  @JsonProperty("create_time")
  public Date getCreateTime() {
    return this.createTime;
  }

  public PaypalPaymentDetail setCreateTime(final Date createTime) {
    this.createTime = createTime;
    return this;
  }

  @JsonProperty("links")
  public List<Link> getLinks() {
    return this.links;
  }

  public PaypalPaymentDetail setLinks(final List<Link> links) {
    this.links = links;
    return this;
  }

  @Override
  public String toString() {
    return "PaypalPaymentDetail{"
        + "id='"
        + id
        + '\''
        + ", intent='"
        + intent
        + '\''
        + ", status='"
        + status
        + '\''
        + ", purchaseUnits="
        + purchaseUnits
        + ", payer="
        + payer
        + ", createTime="
        + createTime
        + ", links="
        + links
        + '}';
  }
}