package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CustomerNotValidException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerNotValidException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Customer is not valid");
  }
}
