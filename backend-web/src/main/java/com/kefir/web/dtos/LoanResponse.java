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
  Integer loanType;
  BigDecimal totalOperationAmount;
  OffsetDateTime openingDate;
  Integer currency;
  OffsetDateTime expirationDate;
  Integer numberOfInstallments;
  LoanStatus status;
  OffsetDateTime updatedAt;
  Integer coreUser;
}
