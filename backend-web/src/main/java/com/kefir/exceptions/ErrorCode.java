package com.kefir.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Account not found"),

  ACCOUNT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Account type not found"),

  AMORTIZATION_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Amortization type not found"),

  BANK_NOT_FOUND(HttpStatus.NOT_FOUND, "Bank not found"),

  BANK_BRANCH_NOT_FOUND(HttpStatus.NOT_FOUND, "Bank branch not found"),

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),

  CURRENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "Currency not found"),

  CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "Customer not found"),
  CUSTOMER_NOT_VALID(HttpStatus.UNPROCESSABLE_ENTITY, "Customer not valid"),
  CUSTOMER_NOT_CREATED(HttpStatus.UNPROCESSABLE_ENTITY, "Customer creation failed"),

  CUSTOMER_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Customer type not found"),
  CUSTOMER_TYPE_NOT_VALID(HttpStatus.UNPROCESSABLE_ENTITY, "Customer type not valid"),

  LOAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Loan not found"),
  LOAN_NOT_CREATED(HttpStatus.UNPROCESSABLE_ENTITY, "Loan creation failed"),

  LOAN_INSTALLMENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Loan installments not found"),

  LOAN_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Loan type not found"),
  LOAN_TYPE_NOT_CREATED(HttpStatus.UNPROCESSABLE_ENTITY, "Loan type creation failed"),
  LOAN_TYPE_INTEREST_RATE_ZERO(
      HttpStatus.UNPROCESSABLE_ENTITY, "Loan type interest cannot be zero"),

  PERSON_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Person type not found"),

  ROLES_NOT_VALID(HttpStatus.UNPROCESSABLE_ENTITY, "One or more roles are not valid"),

  SECURITY_CONTEXT_HOLDER_EXCEPTION(HttpStatus.CONFLICT, "Unexpected error during authentication"),

  SNS_MESSAGE_SENDING_EXCEPTION(HttpStatus.CONFLICT, "SNS message cannot be delivered"),

  USER_NOT_VALID(HttpStatus.UNPROCESSABLE_ENTITY, "User not valid"),

  DOCUMENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Document type not found");

  private final HttpStatus status;
  private final String message;
}
