package com.kefir.web.DTOs;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoanResponse {
  Long id;
  Long customer;
  Integer loanType;
  Double totalOperationAmount;
  LocalDate openingDate;
  Integer currency;
  LocalDate expirationDate;
  Integer totalTermDays;
  LocalDate closedDate;
  Integer closedCode;
  LocalDate nextInstallmentDate;
  Integer status;
  LocalDate lastModificationDate;
  Integer coreUser;
}
