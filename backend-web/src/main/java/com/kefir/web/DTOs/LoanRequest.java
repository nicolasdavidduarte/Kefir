package com.kefir.web.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Represents a loan")
public class LoanRequest {
  @Schema(description = "Loan identification number", example = "15001")
  private final Long id;

  @NotNull
  @Schema(description = "Customer identification number", example = "1025")
  private final Long customer;

  @NotNull private final int loanType;

  @NotNull
  @Schema(description = "Loan amount requested", example = "10000.00")
  private final double totalOperationAmount;

  @NotNull private final LocalDate openingDate;

  @NotNull private final int currency;

  private final LocalDate expirationDate;

  @NotNull private final int totalTermDays;

  private final LocalDate closedDate;
  private final int closedCode;
  private final LocalDate nextInstallmentDate;
  private final int status;
  private final LocalDate lastModificationDate;

  private final int coreUser;

  // Constructor
  public LoanRequest(
      Long id,
      Long customer,
      int loanType,
      double totalOperationAmount,
      LocalDate openingDate,
      int currency,
      LocalDate expirationDate,
      int totalTermDays,
      LocalDate closedDate,
      int closedCode,
      LocalDate nextInstallmentDate,
      int status,
      LocalDate lastModificationDate,
      int coreUser) {
    this.id = id;
    this.customer = customer;
    this.loanType = loanType;
    this.totalOperationAmount = totalOperationAmount;
    this.openingDate = openingDate;
    this.currency = currency;
    this.expirationDate = expirationDate;
    this.totalTermDays = totalTermDays;
    this.closedDate = closedDate;
    this.closedCode = closedCode;
    this.nextInstallmentDate = nextInstallmentDate;
    this.status = status;
    this.lastModificationDate = lastModificationDate;
    this.coreUser = coreUser;
  }
}
