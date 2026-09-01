package com.kefir.exceptions;

public class InvalidPaginationException extends RuntimeException {

  public InvalidPaginationException(String message) {
    super(message);
  }
}
