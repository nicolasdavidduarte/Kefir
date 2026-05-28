package com.kefir.exceptions;

import java.io.Serial;

public class LoanTypeCreationException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanTypeCreationException(String message) {
    super(message);
  }
}
