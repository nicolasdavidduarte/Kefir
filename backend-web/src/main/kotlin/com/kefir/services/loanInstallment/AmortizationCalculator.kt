package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.enums.AmortizationTypeName

interface AmortizationCalculator {
    fun getType(): AmortizationTypeName

    fun generateSchedule(loan: Loan): List<LoanInstallment>
}
