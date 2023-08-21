package com.hba.hbacore.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Name {
  private String fullName;
  private String givenName;
  private String surname;

  @JsonProperty("full_name")
  public String getFullName() {
    return this.fullName;
  }

  public Name setFullName(final String fullName) {
    this.fullName = fullName;
    return this;
  }

  @JsonProperty("given_name")
  public String getGivenName() {
    return this.givenName;
  }

  public Name setGivenName(final String givenName) {
    this.givenName = givenName;
    return this;
  }

  @JsonProperty("surname")
  public String getSurname() {
    return this.surname;
  }

  public Name setSurname(final String surname) {
    this.surname = surname;
    return this;
  }

  @Override
  public String toString() {
    return "Name{"
        + "fullName='"
        + fullName
        + '\''
        + ", givenName='"
        + givenName
        + '\''
        + ", surname='"
        + surname
        + '\''
        + '}';
  }
}