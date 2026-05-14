package com.kefir.exceptions;

public class BankNotFoundException extends RuntimeException {
  public BankNotFoundException(String message) {
    super(message);
  }

  public BankNotFoundException(Exception e) {
    super(e);
  }
}
