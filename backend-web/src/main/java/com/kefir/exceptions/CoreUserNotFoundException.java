package com.kefir.exceptions;

import java.io.Serial;

public class CoreUserNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public CoreUserNotFoundException(String message) {
    super(message);
  }
}
