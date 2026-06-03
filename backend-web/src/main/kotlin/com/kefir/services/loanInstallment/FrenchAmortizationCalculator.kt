package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.enums.AmortizationTypeName
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

@Service
class FrenchAmortizationCalculator : AmortizationCalculator {

    override fun getType() = AmortizationTypeName.FRENCH

    override fun generateSchedule(loan: Loan): List<LoanInstallment> {
        val monthlyInterestRate =
            loan.monthlyInterestRate.divide(
                BigDecimal.valueOf(100),
                10,
                RoundingMode.HALF_UP,
            )

        var balance = loan.principalAmount

        val installmentAmount =
            calculateInstallmentAmount(
                loan.principalAmount,
                monthlyInterestRate,
                loan.numberOfInstallments,
            )

        val schedule = mutableListOf<LoanInstallment>()

        for (installmentNumber in 1..loan.numberOfInstallments) {
            val interest =
                balance.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            var principal =
                installmentAmount.subtract(interest)
                    .setScale(2, RoundingMode.HALF_UP)

            if (installmentNumber == loan.numberOfInstallments) {
                principal = balance
            }

            balance = balance.subtract(principal)
                .setScale(2, RoundingMode.HALF_UP)

            val installment =
                LoanInstallment(
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

            schedule.add(installment)
        }

        return schedule
    }

    private fun calculateInstallmentAmount(
        principal: BigDecimal,
        monthlyInterestRate: BigDecimal,
        installments: Int,
    ): BigDecimal {
        val p = principal.toDouble()
        val i = monthlyInterestRate.toDouble()
        val n = installments.toDouble()

        val installment =
            p * (
                (
                    i * (1 + i).pow(n) /
                        ((1 + i).pow(n) - 1)
                    )
                )

        return BigDecimal.valueOf(installment)
            .setScale(2, RoundingMode.HALF_UP)
    }
}
