package com.kefir.exceptions;

import java.io.Serial;

public class LoanTypeNotFoundException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public LoanTypeNotFoundException() {
    super("Loan type not found");
  }
}
