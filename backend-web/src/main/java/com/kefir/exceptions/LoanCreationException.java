package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class LoanCreationException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanCreationException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Error creating loan");
  }
}
