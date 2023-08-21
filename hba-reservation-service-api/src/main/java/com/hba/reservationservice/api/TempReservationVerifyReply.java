package com.hba.reservationservice.api;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TempReservationVerifyReply {

  private Map<Integer, DetailInfo> suiteInfos;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DetailInfo {

    private int price;
    private String discountId;

    @Override
    public String toString() {
      return "{price=" + price + ", discountId='" + discountId + '\'' + '}';
    }

  }

}