package com.kefir.exceptions;

public class CustomerNotValidException extends RuntimeException {
  public CustomerNotValidException(String message) {
    super(message);
  }

  public CustomerNotValidException(Exception e) {
    super(e);
  }
}
