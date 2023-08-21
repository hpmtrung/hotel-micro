package com.hba.reservationservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuiteDTO {
  int id;
  int typeId;
  String typeName;
  int styleId;
  String styleName;
  int occupation;
  int area;
  String imageUrl;

  public String getName() {
    return typeName + " " + styleName;
  }

}