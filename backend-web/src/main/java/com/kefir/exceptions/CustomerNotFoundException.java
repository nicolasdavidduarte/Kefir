package com.kefir.exceptions;

public class CustomerNotFoundException extends RuntimeException {
  public CustomerNotFoundException(String message) {
    super(message);
  }

  public CustomerNotFoundException(Exception e) {
    super(e);
  }
}
