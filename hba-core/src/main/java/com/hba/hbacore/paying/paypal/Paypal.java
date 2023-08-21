package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Paypal {
  private String emailAddress;
  private String accountId;
  private Name name;
  private Address address;

  @JsonProperty("email_address")
  public String getEmailAddress() {
    return emailAddress;
  }

  public Paypal setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  @JsonProperty("account_id")
  public String getAccountId() {
    return accountId;
  }

  public Paypal setAccountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

  public Name getName() {
    return name;
  }

  public Paypal setName(Name name) {
    this.name = name;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Paypal setAddress(Address address) {
    this.address = address;
    return this;
  }
}