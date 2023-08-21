package com.fs.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkspaceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -2590528307072077187L;

}