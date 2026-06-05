package com.kefir.services.loanInstallment

import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.InstallmentData
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class GermanAmortizationCalculator : BaseAmortizationCalculator() {

    override fun getType() = AmortizationTypeName.GERMAN

    override fun generateSchedule(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ): List<InstallmentData> {
        val monthlyInterestRate =
            monthlyInterestRate.divide(
                BigDecimal.valueOf(100),
                10,
                RoundingMode.HALF_UP,
            )

        var balance = principalAmount

        val fixedAmortization =
            principalAmount.divide(
                BigDecimal.valueOf(numberOfInstallments.toLong()),
                2,
                RoundingMode.HALF_UP,
            )

        val schedule = mutableListOf<InstallmentData>()

        for (installmentNumber in 1..numberOfInstallments) {
            val interest =
                balance.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            var principal = fixedAmortization

            if (installmentNumber == numberOfInstallments) {
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
