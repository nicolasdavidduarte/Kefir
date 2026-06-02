package com.kefir.web.dtos.loan;

import com.kefir.enums.CurrencyIsoCodes;
import com.kefir.enums.LoanTypeName;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record LoanRequest(
    @NotNull(message = "Customer id is mandatory") Long customerId,
    @NotNull(message = "Loan type id is mandatory") LoanTypeName loanType,
    @NotNull(message = "Total operation amount is mandatory") BigDecimal totalOperationAmount,
    @NotNull(message = "Currency ISO code is mandatory") CurrencyIsoCodes currencyIsoCode,
    @NotNull(message = "Number of installments is mandatory") Integer numberOfInstallments,
    @NotNull(message = "External id is mandatory") Long externalId) {}
