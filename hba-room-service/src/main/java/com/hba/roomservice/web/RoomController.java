package com.hba.roomservice.web;

import com.hba.hbacore.util.RequestUtil;
import com.hba.hbacore.util.StringUtil;
import com.hba.roomservice.api.domain.SuiteDTO;
import com.hba.roomservice.domains.room.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

  private final RoomService roomService;

  @GetMapping("/suite_type")
  List<SuiteType> getSuiteTypes() {
    log.info("Getting suite types");
    return roomService.getSuiteTypes();
  }

  @GetMapping("/suite_style")
  List<SuiteStyle> getSuiteStyles() {
    log.info("Getting suite styles");
    return roomService.getSuiteStyles();
  }

  @GetMapping("/amenity")
  List<Amenity> getAmenities() {
    log.info("Getting amenities");
    return roomService.getAmenities();
  }

  @PutMapping("/suite_search_view")
  void insertSuiteSearchView() {
    roomService.updateSuiteSearchView();
  }

  @GetMapping("/suite_search")
  ResponseEntity<List<SuiteDTO>> findSuites(SuiteSearchRequest request, Pageable pageable) {
    log.info("Search suites with criterias {} and pageable {}", request, pageable);
    Page<SuiteDTO> page = roomService.findSuites(request, pageable);
    return RequestUtil.createPaginationResponse(page);
  }

  @GetMapping("/rentable_suite_search")
  RentableSuiteSearchResponse findRentableSuites(
      @RequestParam LocalDate checkinAt,
      @RequestParam LocalDate checkoutAt,
      @RequestParam(name = "occupations") String occupationStr) {
    List<Integer> occupations = StringUtil.underscoreDelimiterToList(occupationStr);
    log.info(
        "Search rentable suites with date [{}-{}] and occupations {}",
        checkinAt,
        checkoutAt,
        occupations);

    return roomService.findRentableSuites(checkinAt, checkoutAt, occupations);
  }
}