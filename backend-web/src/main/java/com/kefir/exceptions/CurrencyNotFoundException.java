package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class CurrencyNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public CurrencyNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Currency not found");
  }
}
