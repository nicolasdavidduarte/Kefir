package com.kefir.exceptions;

import java.io.Serial;

public class CustomerTypeNotValidException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerTypeNotValidException(String message) {
    super(message);
  }
}
