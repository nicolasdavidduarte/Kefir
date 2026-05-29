package com.kefir.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;

public class SNSMessageSendingException extends ApiException {
  @Serial private static final long serialVersionUID = 1L;

  public SNSMessageSendingException() {
    super(HttpStatus.CONFLICT, "SNS message cannot be delivered");
  }
}
