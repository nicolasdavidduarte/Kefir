package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CustomerCreationException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerCreationException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Error creating customer");
  }
}
