package com.kefir.services.loanInstallment

import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.InstallmentData
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AmericanAmortizationCalculator : BaseAmortizationCalculator() {

    override fun getType() = AmortizationTypeName.AMERICAN

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

        val schedule = mutableListOf<InstallmentData>()

        var balance = principalAmount

        for (installmentNumber in 1..numberOfInstallments) {
            val interest =
                balance.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            val principal =
                if (installmentNumber == numberOfInstallments) {
                    balance
                } else {
                    BigDecimal.ZERO
                }

            val installmentAmount =
                principal.add(interest)
                    .setScale(2, RoundingMode.HALF_UP)

            val balance =
                if (installmentNumber == numberOfInstallments) {
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
