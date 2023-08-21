package com.hba.roomservice.domains.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class RentableSuiteSearchResponse {

  List<List<Integer>> occupationMapSuiteIds;
  List<RentableSuiteDTO> reservableSuites;

}