package com.kefir.exceptions;

import java.io.Serial;

public class LoanNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanNotFoundException(Long loanId) {
    super(String.format("Loan with ID %d not found", loanId));
  }
}
