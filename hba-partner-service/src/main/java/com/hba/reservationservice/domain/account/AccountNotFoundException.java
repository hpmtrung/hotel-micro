package com.hba.reservationservice.domain.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AccountNotFoundException(int id) {
    super(String.format("Account with id '%d' not found", id));
  }

}