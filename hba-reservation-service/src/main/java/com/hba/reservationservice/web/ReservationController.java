package com.hba.reservationservice.web;

import com.hba.hbacore.model.User;
import com.hba.reservationservice.api.ReservationDTO;
import com.hba.reservationservice.domain.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping("/create_temp")
  ReservationDTO createTempReservation(
      @RequestBody TempReservationCreateRequest tempReservationCreateRequest,
      @AuthenticationPrincipal User user) {
    log.info(
        "Creating a new temporary reservation with info {} and user {}",
        tempReservationCreateRequest,
        user);
    return reservationService.createTempReservation(tempReservationCreateRequest, user);
  }

  @PostMapping("/complete_deposit")
  ReservationDTO completeTempReservation(@RequestParam Integer reservationId) {
    log.info("Paying deposit for temporary reservation with id '{}'", reservationId);
    return reservationService.completeTempReservation(reservationId);
  }
}