package com.kefir.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

  @NotNull private final Integer loanType;

  @NotNull
  @Schema(description = "Loan amount requested", example = "10000.00")
  private final BigDecimal totalOperationAmount;

  @NotNull private final Integer currency;

  @NotNull private final OffsetDateTime openingDate;

  private final OffsetDateTime expirationDate;

  @NotNull private final Integer numberOfInstallments;

  private final Integer statusId;

  private OffsetDateTime updatedAt = OffsetDateTime.now();

  private final Integer coreUser;

  // Constructor
  public LoanRequest(
      Long id,
      Long customer,
      Integer loanType,
      BigDecimal totalOperationAmount,
      OffsetDateTime openingDate,
      Integer currency,
      OffsetDateTime expirationDate,
      Integer numberOfInstallments,
      Integer statusId,
      Integer coreUserId) {
    this.id = id;
    this.customer = customer;
    this.loanType = loanType;
    this.totalOperationAmount = totalOperationAmount;
    this.openingDate = openingDate;
    this.currency = currency;
    this.expirationDate = expirationDate;
    this.numberOfInstallments = numberOfInstallments;
    this.statusId = statusId;
    this.coreUser = coreUserId;
  }
}
