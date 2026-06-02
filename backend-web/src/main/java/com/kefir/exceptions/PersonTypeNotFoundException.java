package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class PersonTypeNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public PersonTypeNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Person type not found");
  }
}
