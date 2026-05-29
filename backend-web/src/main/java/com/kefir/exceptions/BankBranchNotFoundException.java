package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class BankBranchNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public BankBranchNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Bank branch not found");
  }
}
