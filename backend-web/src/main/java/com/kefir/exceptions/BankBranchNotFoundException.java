package com.kefir.exceptions;

import java.io.Serial;

public class BankBranchNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public BankBranchNotFoundException(String message) {
    super(message);
  }
}
