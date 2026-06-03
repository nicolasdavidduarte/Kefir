package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class AmortizationTypeNotFoundException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public AmortizationTypeNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Amortization type not found");
  }
}
