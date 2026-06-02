package com.kefir.web.dtos.loan;

import com.kefir.entities.Loan;
import com.kefir.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record LoanResponse(
    Long id,
    String customer,
    String loanType,
    String currency,
    Integer numberOfInstallments,
    BigDecimal totalOperationAmount,
    OffsetDateTime openingDate,
    OffsetDateTime expirationDate,
    LoanStatus status,
    OffsetDateTime createdAt,
    String user) {

  public static LoanResponse fromEntity(Loan loan) {
    return new LoanResponse(
        loan.getId(),
        loan.getCustomer().getFullname(),
        loan.getLoanType().getName(),
        loan.getCurrency().getIsoCode(),
        loan.getNumberOfInstallments(),
        loan.getTotalOperationAmount(),
        loan.getOpeningDate(),
        loan.getExpirationDate(),
        loan.getStatus(),
        loan.getCreatedAt(),
        loan.getUser().getUsername());
  }
}
