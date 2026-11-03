package com.kefir.exceptions;

import lombok.Getter;

@Getter
public enum DomainErrorCode {
  LOAN_NOT_VALID("Loan is not in a valid state", DomainErrorType.BUSINESS_RULE),
  INVALID_AMOUNT("Amount must be positive", DomainErrorType.VALIDATION),
  INSUFFICIENT_FUNDS("Insufficient funds", DomainErrorType.BUSINESS_RULE);

  private final String message;
  private final DomainErrorType type;

  DomainErrorCode(String message, DomainErrorType type) {
    this.message = message;
    this.type = type;
  }
}
