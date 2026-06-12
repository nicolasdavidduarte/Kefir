package com.kefir.services.loanInstallment

import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.InstallmentData
import java.math.BigDecimal

interface AmortizationCalculator {
    val type: AmortizationTypeName

    fun generateSchedule(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ): List<InstallmentData>
}
