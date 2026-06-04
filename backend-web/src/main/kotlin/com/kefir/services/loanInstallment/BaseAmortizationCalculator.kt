package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import java.math.BigDecimal

abstract class BaseAmortizationCalculator : AmortizationCalculator {

    protected fun createInstallment(
        loan: Loan,
        installmentNumber: Int,
        installmentAmount: BigDecimal,
        principal: BigDecimal,
        interest: BigDecimal,
        balance: BigDecimal,
    ): LoanInstallment = LoanInstallment(
        loan = loan,
        number = installmentNumber,
        totalAmount = installmentAmount,
        principalAmount = principal,
        interestAmount = interest,
        remainingBalance = balance.max(BigDecimal.ZERO),
        paymentDueDate = loan.openingDate.plusMonths(
            installmentNumber.toLong(),
        ),
        user = loan.user,
    )
}
