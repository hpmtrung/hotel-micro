package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
  private String addressLine1 = "";
  private String addressLine2 = "";
  private String adminArea2 = "";
  private String adminArea1 = "";
  private String postalCode = "";
  private String countryCode = "";

  @JsonProperty("address_line_2")
  public String getAddressLine2() {
    return this.addressLine2;
  }

  public Address setAddressLine2(final String addressLine2) {
    this.addressLine2 = addressLine2;
    return this;
  }

  @JsonProperty("address_line_1")
  public String getAddressLine1() {
    return this.addressLine1;
  }

  public Address setAddressLine1(final String addressLine1) {
    this.addressLine1 = addressLine1;
    return this;
  }

  @JsonProperty("admin_area_2")
  public String getAdminArea2() {
    return this.adminArea2;
  }

  public Address setAdminArea2(final String adminArea2) {
    this.adminArea2 = adminArea2;
    return this;
  }

  @JsonProperty("admin_area_1")
  public String getAdminArea1() {
    return this.adminArea1;
  }

  public Address setAdminArea1(final String adminArea1) {
    this.adminArea1 = adminArea1;
    return this;
  }

  @JsonProperty("postal_code")
  public String getPostalCode() {
    return this.postalCode;
  }

  public Address setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

  @JsonProperty("country_code")
  public String getCountryCode() {
    return this.countryCode;
  }

  public Address setCountryCode(final String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  @Override
  public String toString() {
    return "Address{"
        + "addressLine1='"
        + addressLine1
        + '\''
        + "addressLine2='"
        + addressLine2
        + '\''
        + ", adminArea2='"
        + adminArea2
        + '\''
        + ", adminArea1='"
        + adminArea1
        + '\''
        + ", postalCode='"
        + postalCode
        + '\''
        + ", countryCode='"
        + countryCode
        + '\''
        + '}';
  }
}