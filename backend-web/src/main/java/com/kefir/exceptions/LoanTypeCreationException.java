package com.kefir.exceptions;

public class LoanTypeCreationException extends RuntimeException {
  public LoanTypeCreationException(String message) {
    super(message);
  }

  public LoanTypeCreationException(Exception e) {
    super(e);
  }
}
