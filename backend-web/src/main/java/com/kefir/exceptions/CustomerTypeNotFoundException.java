package com.kefir.exceptions;

import java.io.Serial;

public class CustomerTypeNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerTypeNotFoundException(String message) {
    super(message);
  }
}
