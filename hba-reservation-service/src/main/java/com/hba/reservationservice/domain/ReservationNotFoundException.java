package com.hba.reservationservice.domain;

public class ReservationNotFoundException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public ReservationNotFoundException(int id) {
    super("Reservation not found with id '" + id +"'");
  }

}