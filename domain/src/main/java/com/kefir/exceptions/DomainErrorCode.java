package com.kefir.exceptions;

import lombok.Getter;

@Getter
public enum DomainErrorCode {

  // LOAN
  LOAN_NOT_VALID("Loan is not in a valid state", DomainErrorType.BUSINESS_RULE),
  INVALID_AMOUNT("Amount must be positive", DomainErrorType.VALIDATION),
  INSUFFICIENT_FUNDS(
      "Account does not have the necessary funds to complete the operation",
      DomainErrorType.BUSINESS_RULE),

  INVALID_INSTALLMENTS(
      "The number of installments cannot be less than 1", DomainErrorType.BUSINESS_RULE),

  ACCOUNT_NOT_VALID("Account is not in a valid state", DomainErrorType.BUSINESS_RULE),

  ACCOUNT_NOT_VALID_FOR_CLOSURE("Account is not valid for closure", DomainErrorType.BUSINESS_RULE),

  CUSTOMER_NOT_VALID("Customer is not in a valid state", DomainErrorType.BUSINESS_RULE);

  private final String message;
  private final DomainErrorType type;

  DomainErrorCode(String message, DomainErrorType type) {
    this.message = message;
    this.type = type;
  }
}
