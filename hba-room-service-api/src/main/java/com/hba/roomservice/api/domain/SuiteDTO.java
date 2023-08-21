package com.hba.roomservice.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
  int originalPrice;
  String imageUrl;
  int rentNum;
  DiscountDTO discount;
  int discountPercent;
  int actualPrice;
  List<Integer> amenityIds;

  public SuiteDTO(SuiteProjection p) {
    this.id = p.getId();
    this.typeId = p.getTypeId();
    this.typeName = p.getTypeName();
    this.styleId = p.getStyleId();
    this.styleName = p.getStyleName();
    this.occupation = p.getOccupation();
    this.area = p.getArea();
    this.originalPrice = p.getPrice();
    this.imageUrl =
        String.format(
            "http://localhost:8055/api/storage/object?workspaceId=6&key=%s", p.getImageUrl());
    this.rentNum = p.getRentNum();
    this.amenityIds =
        Arrays.stream(p.getAmenityIds().split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    if (p.getDiscountId() == null) {
      this.discount = null;
      this.discountPercent = 0;
      this.actualPrice = this.originalPrice;
    } else {
      this.discount =
          new DiscountDTO(p.getDiscountId(), p.getStartAt(), p.getEndAt(), p.getCreatedAt());
      this.discountPercent = p.getPercent();
      this.actualPrice =
          (int) (Math.round(this.originalPrice * (this.discountPercent / 100.0) / 500.0) * 500);
    }
  }

  public String getName() {
    return styleName + " " + typeName;
  }
}