package com.kefir.exceptions;

public class BankBranchNotFoundException extends RuntimeException {
  public BankBranchNotFoundException(String message) {
    super(message);
  }

  public BankBranchNotFoundException(Exception e) {
    super(e);
  }
}
