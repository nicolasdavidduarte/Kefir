package com.kefir.web.dtos;

import com.kefir.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.*;

@Builder
public record LoanResponse(
    Long id,
    Long customer,
    String loanType,
    String currency,
    Integer numberOfInstallments,
    BigDecimal totalOperationAmount,
    OffsetDateTime openingDate,
    OffsetDateTime expirationDate,
    LoanStatus status,
    OffsetDateTime createdAt,
    String user) {}
