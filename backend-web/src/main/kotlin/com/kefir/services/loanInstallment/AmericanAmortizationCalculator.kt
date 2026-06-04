package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.InstallmentData
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AmericanAmortizationCalculator : BaseAmortizationCalculator() {

    override fun getType() = AmortizationTypeName.AMERICAN

    override fun generateSchedule(
        loan: Loan,
    ): List<InstallmentData> {
        val monthlyInterestRate =
            loan.monthlyInterestRate.divide(
                BigDecimal.valueOf(100),
                10,
                RoundingMode.HALF_UP,
            )

        val schedule = mutableListOf<InstallmentData>()

        var balance = loan.principalAmount

        for (installmentNumber in 1..loan.numberOfInstallments) {
            val interest =
                balance.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            val principal =
                if (installmentNumber == loan.numberOfInstallments) {
                    balance
                } else {
                    BigDecimal.ZERO
                }

            val installmentAmount =
                principal.add(interest)
                    .setScale(2, RoundingMode.HALF_UP)

            val balance =
                if (installmentNumber == loan.numberOfInstallments) {
                    BigDecimal.ZERO
                } else {
                    balance
                }

            schedule.add(
                InstallmentData(
                    number = installmentNumber,
                    totalAmount = installmentAmount,
                    principalAmount = principal,
                    interestAmount = interest,
                    remainingBalance = balance,
                ),
            )
        }

        return schedule
    }
}
