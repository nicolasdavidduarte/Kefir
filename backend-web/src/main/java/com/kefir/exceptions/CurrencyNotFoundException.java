package com.kefir.exceptions;

import java.io.Serial;

public class CurrencyNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public CurrencyNotFoundException(String message) {
    super(message);
  }
}
