package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class BankNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public BankNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Bank not found");
  }
}
