package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hba.hbacore.paying.PaymentResponse;
import org.springframework.hateoas.Link;

import java.util.Date;
import java.util.List;

public class PaypalPaymentResponse implements PaymentResponse {
  private String id;
  private String intent;
  private String status;
  private List<PurchaseUnit> purchaseUnits;
  private Date createTime;
  private List<Link> links;
  private Payer payer;
  private PaymentSource paymentSource;

  @JsonProperty("payment_source")
  public PaymentSource getPaymentSource() {
    return paymentSource;
  }

  public PaypalPaymentResponse setPaymentSource(PaymentSource paymentSource) {
    this.paymentSource = paymentSource;
    return this;
  }

  public Payer getPayer() {
    return payer;
  }

  public PaypalPaymentResponse setPayer(final Payer payer) {
    this.payer = payer;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("intent")
  public String getIntent() {
    return this.intent;
  }

  public void setIntent(String intent) {
    this.intent = intent;
  }

  @JsonProperty("status")
  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("purchase_units")
  public List<PurchaseUnit> getPurchaseUnits() {
    return this.purchaseUnits;
  }

  public void setPurchaseUnits(List<PurchaseUnit> purchaseUnits) {
    this.purchaseUnits = purchaseUnits;
  }

  @JsonProperty("create_time")
  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @JsonProperty("links")
  public List<Link> getLinks() {
    return this.links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  @Override
  public String toString() {
    return "PaypalPaymentResponse{"
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
        + ", createTime="
        + createTime
        + ", links="
        + links
        + ", payer="
        + payer
        + '}';
  }

  @Override
  public long getInvoiceId() {
    return Long.parseLong(purchaseUnits.get(0).getInvoiceId());
  }
}