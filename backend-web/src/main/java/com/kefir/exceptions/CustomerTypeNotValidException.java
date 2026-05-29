package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CustomerTypeNotValidException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerTypeNotValidException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Customer typer is not valid for the operation");
  }
}
