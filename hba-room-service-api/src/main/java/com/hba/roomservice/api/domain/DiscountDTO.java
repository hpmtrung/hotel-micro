package com.hba.roomservice.api.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class DiscountDTO {
  String id;
  LocalDateTime startAt;
  LocalDateTime endAt;
  LocalDateTime createdAt;
}