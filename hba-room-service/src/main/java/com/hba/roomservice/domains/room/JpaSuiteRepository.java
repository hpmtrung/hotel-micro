package com.hba.roomservice.domains.room;

import com.hba.roomservice.api.domain.SuiteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JpaSuiteRepository extends JpaRepository<Suite, Integer> {

  @Query(value = "select * from uv_suite_search", nativeQuery = true)
  List<SuiteProjection> getSuiteSearchView();

  @Query(value = "select * from uf_find_rentable_suites_on_date(?1, ?2)", nativeQuery = true)
  List<RentableSuiteRecordProjection> findRentableSuitesByDate(
      LocalDate checkinAt, LocalDate checkoutAt);
}