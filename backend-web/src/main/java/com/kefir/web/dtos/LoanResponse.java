package com.kefir.web.dtos;

import com.kefir.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoanResponse {
  Long id;
  Long customer;
  String loanType;
  BigDecimal totalOperationAmount;
  OffsetDateTime openingDate;
  String currency;
  OffsetDateTime expirationDate;
  Integer numberOfInstallments;
  LoanStatus status;
  OffsetDateTime createdAt;
  String user;
}
