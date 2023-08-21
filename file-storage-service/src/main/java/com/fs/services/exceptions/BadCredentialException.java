package com.fs.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadCredentialException extends RuntimeException {

  private static final long serialVersionUID = 3382753156725785575L;

}