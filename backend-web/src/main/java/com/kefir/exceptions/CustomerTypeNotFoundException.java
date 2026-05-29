package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CustomerTypeNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CustomerTypeNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Customer type not found");
  }
}
