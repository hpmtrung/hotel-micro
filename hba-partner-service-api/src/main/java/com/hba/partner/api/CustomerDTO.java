package com.hba.partner.api;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDTO {
  private String personalId;
  private String name;
  private String address;
  private String phoneNumber;
  private String taxCode;
  private Integer accountId;

  public CustomerDTO(String personalId) {
    this.personalId = personalId;
  }

}