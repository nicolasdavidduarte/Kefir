package com.kefir.exceptions;

import java.io.Serial;

public class SecurityContextHolderException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public SecurityContextHolderException() {
    super("Unexpected error during authentication");
  }
}
