package org.kefir.DTOs;

import java.util.Date;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoanDataDTO {
  Long id;
  Integer customer;
  Integer loanType;
  Double totalOperationAmount;
  Date openingDate;
  Integer currency;
  Date expirationDate;
  Integer totalTermDays;
  Date closedDate;
  Integer closedCode;
  Date nextInstallmentDate;
  Integer status;
  Date lastModificationDate;
  Integer coreUser;
}
