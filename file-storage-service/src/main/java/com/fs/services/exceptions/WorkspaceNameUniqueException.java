package com.fs.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WorkspaceNameUniqueException extends RuntimeException {

  private static final long serialVersionUID = -7832357650592437703L;

}