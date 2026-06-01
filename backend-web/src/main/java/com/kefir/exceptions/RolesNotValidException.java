package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class RolesNotValidException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public RolesNotValidException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "One or more roles are not valid");
  }
}
