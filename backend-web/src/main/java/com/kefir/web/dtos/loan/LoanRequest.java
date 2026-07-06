package com.kefir.web.dtos.loan;

import com.kefir.enums.AmortizationTypeName;
import com.kefir.enums.CurrencyIsoCodes;
import com.kefir.enums.LoanTypeName;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record LoanRequest(
    @NotNull(message = "Customer id is mandatory") Long customerId,
    @NotNull(message = "Account id is mandatory") Long accountId,
    @NotNull(message = "Loan type id is mandatory") LoanTypeName loanType,
    @NotNull(message = "Amortization type id is mandatory") AmortizationTypeName amortizationType,
    @NotNull(message = "Principal amount is mandatory") BigDecimal principalAmount,
    @NotNull(message = "Currency ISO code is mandatory") CurrencyIsoCodes currencyIsoCode,
    @NotNull(message = "Number of installments is mandatory") Integer numberOfInstallments,
    Long externalId) {}
