package com.hba.roomservice.api.domain;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface SuiteProjection {
  Integer getId();

  @Value("#{target.type_id}")
  Integer getTypeId();

  @Value("#{target.style_id}")
  Integer getStyleId();

  Integer getArea();

  Integer getPrice();

  @Value("#{target.image_url}")
  String getImageUrl();

  @Value("#{target.rent_num}")
  Integer getRentNum();

  @Value("#{target.style_name}")
  String getStyleName();

  @Value("#{target.type_name}")
  String getTypeName();

  Integer getOccupation();

  @Value("#{target.amenity_ids}")
  String getAmenityIds();

  @Value("#{target.discount_id}")
  String getDiscountId();

  Integer getPercent();

  @Value("#{target.start_at}")
  LocalDateTime getStartAt();

  @Value("#{target.end_at}")
  LocalDateTime getEndAt();

  @Value("#{target.created_at}")
  LocalDateTime getCreatedAt();
}