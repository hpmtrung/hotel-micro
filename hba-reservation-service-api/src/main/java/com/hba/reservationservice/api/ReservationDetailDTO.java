package com.hba.reservationservice.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationDetailDTO {
  private SuiteDTO suite;
  private int roomNum;
  private Integer suitePrice;
  private Integer subTotal;
  private String discountId;
}