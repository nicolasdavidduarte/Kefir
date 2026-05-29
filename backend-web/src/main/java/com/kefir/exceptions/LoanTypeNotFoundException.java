package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class LoanTypeNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanTypeNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Loan type not found");
  }
}
