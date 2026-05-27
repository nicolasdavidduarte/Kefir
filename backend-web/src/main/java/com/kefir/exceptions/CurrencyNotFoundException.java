package com.kefir.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
  public CurrencyNotFoundException(String message) {
    super(message);
  }

  public CurrencyNotFoundException(Exception e) {
    super(e);
  }
}
