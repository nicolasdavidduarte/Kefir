package com.kefir.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

  private final HttpStatus status;
  private final String error;

  protected ApiException(HttpStatus status, String error) {
    this.status = status;
    this.error = error;
  }
}
