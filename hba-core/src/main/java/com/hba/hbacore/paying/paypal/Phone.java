package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Phone {
  private String phoneNumber;

  public Phone setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  @JsonProperty("phone_number")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public String toString() {
    return "Phone{" +
        "phoneNumber='" + phoneNumber + '\'' +
        '}';
  }
}