package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class SecurityContextHolderException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public SecurityContextHolderException() {
    super(HttpStatus.CONFLICT, "Unexpected error during authentication");
  }
}
