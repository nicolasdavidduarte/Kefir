package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class UserNotValidException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public UserNotValidException() {
    super(HttpStatus.UNAUTHORIZED, "User is not in a valid state");
  }
}
