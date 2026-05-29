package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class LoanNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Loan not found");
  }
}
