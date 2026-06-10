package com.kefir.web.dtos.loan;

import com.kefir.entities.Loan;
import com.kefir.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record LoanResponse(
    Long id,
    Long externalId,
    String customer,
    String loanType,
    String amortizationType,
    String currency,
    Integer numberOfInstallments,
    BigDecimal annualInterestRate,
    BigDecimal monthlyInterestRate,
    BigDecimal totalPrincipal,
    BigDecimal totalInterest,
    BigDecimal totalOperationAmount,
    OffsetDateTime openingDate,
    OffsetDateTime expirationDate,
    LoanStatus status,
    OffsetDateTime createdAt,
    String user) {

  public static LoanResponse fromEntity(Loan loan) {
    return new LoanResponse(
        loan.getId(),
        loan.getExternalId(),
        loan.getCustomer().getFullname(),
        loan.getLoanType().getName(),
        loan.getAmortizationType().getName().name(),
        loan.getCurrency().getIsoCode(),
        loan.getNumberOfInstallments(),
        loan.getAnnualInterestRate(),
        loan.getMonthlyInterestRate(),
        loan.getPrincipalAmount(),
        loan.getInterestAmount(),
        loan.getTotalOperationAmount(),
        loan.getOpeningDate(),
        loan.getExpirationDate(),
        loan.getStatus(),
        loan.getCreatedAt(),
        loan.getUser().getUsername());
  }
}
