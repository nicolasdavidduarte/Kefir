package com.kefir.exceptions;

public class CustomerCreationException extends RuntimeException {
  public CustomerCreationException(String message) {
    super(message);
  }

  public CustomerCreationException(Exception e) {
    super(e);
  }
}
