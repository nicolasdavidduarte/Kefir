package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class LoanTypeCreationException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanTypeCreationException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Error creating loan type");
  }
}
