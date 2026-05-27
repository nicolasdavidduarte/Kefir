package com.kefir.exceptions;

public class LoanTypeNotFoundException extends RuntimeException {
  public LoanTypeNotFoundException(String message) {
    super(message);
  }

  public LoanTypeNotFoundException() {
    super("Loan type not found");
  }
}
