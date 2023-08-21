package com.hba.roomservice.domains.room;

import lombok.Data;

@Data
public class SuiteSearchRequest {
  private String styleIds;
  private String typeIds;
  private Boolean hasPromotion;
  private Integer priceFrom;
  private Integer priceTo;
}