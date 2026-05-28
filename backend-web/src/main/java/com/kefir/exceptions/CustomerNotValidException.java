package com.kefir.exceptions;

import java.io.Serial;

public class CustomerNotValidException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerNotValidException(String message) {
    super(message);
  }
}
