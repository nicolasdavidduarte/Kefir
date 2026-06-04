package com.kefir.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private final ErrorCode errorCode;

  public ApiException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public HttpStatus getStatus() {
    return errorCode.getStatus();
  }
}
