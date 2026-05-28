package com.kefir.exceptions;

import java.io.Serial;

public class BankNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public BankNotFoundException(String message) {
    super(message);
  }
}
