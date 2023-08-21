package com.hba.rentalservice.web;

import com.hba.rentalservice.domains.rental.RentalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/rental")
public class RentalController {

  @PostMapping("/to_rental")
  RentalDTO saveReservationToRental(
      @RequestParam Integer reservationId, @RequestBody ReservationSaveToRentalRequest request) {
    log.info("Checkin reservation with id {} and request {}", reservationId, request);
    return null;
  }
}