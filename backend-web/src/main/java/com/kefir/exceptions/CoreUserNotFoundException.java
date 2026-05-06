package com.kefir.exceptions;

public class CoreUserNotFoundException extends RuntimeException {
  public CoreUserNotFoundException(String message) {
    super(message);
  }

  public CoreUserNotFoundException() {
    super("Core user not found");
  }
}
