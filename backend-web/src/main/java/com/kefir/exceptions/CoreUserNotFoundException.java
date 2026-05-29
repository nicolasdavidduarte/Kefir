package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CoreUserNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CoreUserNotFoundException() {
    super(HttpStatus.NOT_FOUND, "User not found");
  }
}
