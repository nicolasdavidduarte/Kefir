package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.InstallmentData
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class GermanAmortizationCalculator : BaseAmortizationCalculator() {

    override fun getType() = AmortizationTypeName.GERMAN

    override fun generateSchedule(loan: Loan): List<InstallmentData> {
        val monthlyInterestRate =
            loan.monthlyInterestRate.divide(
                BigDecimal.valueOf(100),
                10,
                RoundingMode.HALF_UP,
            )

        var balance = loan.principalAmount

        val fixedAmortization =
            loan.principalAmount.divide(
                BigDecimal.valueOf(loan.numberOfInstallments.toLong()),
                2,
                RoundingMode.HALF_UP,
            )

        val schedule = mutableListOf<InstallmentData>()

        for (installmentNumber in 1..loan.numberOfInstallments) {
            val interest =
                balance.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            var principal = fixedAmortization

            if (installmentNumber == loan.numberOfInstallments) {
                principal = balance
            }

            val installmentAmount =
                principal.add(interest)
                    .setScale(2, RoundingMode.HALF_UP)

            balance = balance.subtract(principal)
                .setScale(2, RoundingMode.HALF_UP)

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
