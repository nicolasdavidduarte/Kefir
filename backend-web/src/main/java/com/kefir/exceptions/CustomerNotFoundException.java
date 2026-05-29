package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Customer not found");
  }
}
