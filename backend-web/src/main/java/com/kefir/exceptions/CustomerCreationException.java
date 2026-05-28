package com.kefir.exceptions;

import java.io.Serial;

public class CustomerCreationException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerCreationException(String message) {
    super(message);
  }
}
