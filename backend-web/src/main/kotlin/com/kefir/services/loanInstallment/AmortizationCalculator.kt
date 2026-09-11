package com.kefir.services.loanInstallment

import com.kefir.enums.AmortizationTypeName
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.web.dtos.loanInstallment.InstallmentData
import java.math.BigDecimal
import java.math.RoundingMode

interface AmortizationCalculator {
    val type: AmortizationTypeName

    fun generateSchedule(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ): List<InstallmentData>

    fun validateAndNormalizeRate(monthlyInterestRate: BigDecimal): BigDecimal {
        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            throw ApiException(ErrorCode.LOAN_TYPE_INTEREST_RATE_ZERO)
        }
        return monthlyInterestRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
    }
}
