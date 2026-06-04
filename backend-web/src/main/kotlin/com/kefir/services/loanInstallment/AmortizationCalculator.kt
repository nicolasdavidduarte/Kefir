package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.InstallmentData

interface AmortizationCalculator {
    fun getType(): AmortizationTypeName

    fun generateSchedule(loan: Loan): List<InstallmentData>
}
