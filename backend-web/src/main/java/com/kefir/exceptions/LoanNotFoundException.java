package com.kefir.exceptions;

public class LoanNotFoundException extends RuntimeException {
  public LoanNotFoundException(String message) {
    super(message);
  }

  public LoanNotFoundException(Long loanId) {
    super(String.format("Loan with ID %d not found", loanId));
  }
}
