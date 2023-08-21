package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;

public class Payer {
  private Name name;

  @Pattern(
      regexp =
          "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
  private String emailAddress = "";

  private String payerId = "";
  private Address address;
  private Phone phone;

  @JsonProperty("name")
  public Name getName() {
    return this.name;
  }

  public Payer setName(final Name name) {
    this.name = name;
    return this;
  }

  @JsonProperty("phone")
  public Phone getPhone() {
    return phone;
  }

  public Payer setPhone(final Phone phone) {
    this.phone = phone;
    return this;
  }

  @JsonProperty("email_address")
  public String getEmailAddress() {
    return this.emailAddress;
  }

  public Payer setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  @JsonProperty("payer_id")
  public String getPayerId() {
    return this.payerId;
  }

  public Payer setPayerId(final String payerId) {
    this.payerId = payerId;
    return this;
  }

  @JsonProperty("address")
  public Address getAddress() {
    return this.address;
  }

  public Payer setAddress(final Address address) {
    this.address = address;
    return this;
  }

  @Override
  public String toString() {
    return "Payer{"
        + "name="
        + name
        + ", emailAddress='"
        + emailAddress
        + '\''
        + ", payerId='"
        + payerId
        + '\''
        + ", address="
        + address
        + '}';
  }
}