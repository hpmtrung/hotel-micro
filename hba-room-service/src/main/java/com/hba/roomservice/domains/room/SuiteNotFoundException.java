package com.hba.roomservice.domains.room;

public class SuiteNotFoundException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public SuiteNotFoundException(int id) {
    super("Suite id '" + id + "' not found");
  }

}