package com.kefir.web.dtos;

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
  Integer numberOfInstallments;
  LocalDate closedDate;
  Integer closedCode;
  LocalDate nextInstallmentDate;
  Integer status;
  LocalDate lastModificationDate;
  Integer coreUser;
}
