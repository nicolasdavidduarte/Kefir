package com.kefir.exceptions;

import java.io.Serial;

public class SNSMessageSendingException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;

  public SNSMessageSendingException() {
    super("SNS message cannot be delivered");
  }
}
