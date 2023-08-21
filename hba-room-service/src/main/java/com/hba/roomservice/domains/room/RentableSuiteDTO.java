package com.hba.roomservice.domains.room;

import com.hba.roomservice.api.domain.DiscountDTO;
import com.hba.roomservice.api.domain.SuiteDTO;

import java.util.List;

public class RentableSuiteDTO extends SuiteDTO {
  private int emptyRoomNum;

  public RentableSuiteDTO(
      int id,
      int typeId,
      String typeName,
      int styleId,
      String styleName,
      int occupation,
      int area,
      int originalPrice,
      String imageUrl,
      int rentNum,
      DiscountDTO discount,
      int discountPercent,
      int actualPrice,
      List<Integer> amenityIds) {
    super(
        id,
        typeId,
        typeName,
        styleId,
        styleName,
        occupation,
        area,
        originalPrice,
        imageUrl,
        rentNum,
        discount,
        discountPercent,
        actualPrice,
        amenityIds);
  }

  public RentableSuiteDTO() {}

  public int getEmptyRoomNum() {
    return emptyRoomNum;
  }

  public RentableSuiteDTO setEmptyRoomNum(int emptyRoomNum) {
    this.emptyRoomNum = emptyRoomNum;
    return this;
  }
}