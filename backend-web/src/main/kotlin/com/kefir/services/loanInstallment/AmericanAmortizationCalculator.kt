package com.kefir.services.loanInstallment

import com.kefir.enums.AmortizationTypeName
import com.kefir.web.dtos.loanInstallment.InstallmentData
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AmericanAmortizationCalculator : AmortizationCalculator {

    override val type = AmortizationTypeName.AMERICAN

    override fun generateSchedule(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ): List<InstallmentData> {
        val monthlyInterestRate = validateAndNormalizeRate(monthlyInterestRate)

        val schedule = mutableListOf<InstallmentData>()

        for (installmentNumber in 1..numberOfInstallments) {
            val interest =
                principalAmount.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            val principal =
                if (installmentNumber == numberOfInstallments) {
                    principalAmount
                } else {
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                }

            val installmentAmount =
                principal.add(interest)
                    .setScale(2, RoundingMode.HALF_UP)

            val balance =
                if (installmentNumber == numberOfInstallments) {
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                } else {
                    principalAmount
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
