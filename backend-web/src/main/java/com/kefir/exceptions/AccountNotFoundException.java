package com.kefir.exceptions;

public class AccountNotFoundException extends RuntimeException {
  public AccountNotFoundException(String message) {
    super(message);
  }

  public AccountNotFoundException(Exception e) {
    super(e);
  }
}
