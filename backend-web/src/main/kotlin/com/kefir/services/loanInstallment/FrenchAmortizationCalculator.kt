package com.kefir.services.loanInstallment

import com.kefir.enums.AmortizationTypeName
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.web.dtos.InstallmentData
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class FrenchAmortizationCalculator : BaseAmortizationCalculator() {

    override fun getType() = AmortizationTypeName.FRENCH

    override fun generateSchedule(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ): List<InstallmentData> {
        if (monthlyInterestRate == BigDecimal.ZERO) {
            throw ApiException(ErrorCode.LOAN_TYPE_INTEREST_RATE_ZERO)
        }

        val monthlyInterestRate =
            monthlyInterestRate.divide(
                BigDecimal.valueOf(100),
                10,
                RoundingMode.HALF_UP,
            )

        var balance = principalAmount

        val installmentAmount =
            calculateInstallmentAmount(
                principalAmount,
                monthlyInterestRate,
                numberOfInstallments,
            )

        val schedule = mutableListOf<InstallmentData>()

        for (installmentNumber in 1..numberOfInstallments) {
            val interest =
                balance.multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP)

            var principal =
                installmentAmount.subtract(interest)
                    .setScale(2, RoundingMode.HALF_UP)

            if (installmentNumber == numberOfInstallments) {
                principal = balance
            }

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

    private fun calculateInstallmentAmount(
        principal: BigDecimal,
        monthlyInterestRate: BigDecimal,
        installments: Int,
    ): BigDecimal {
        // French formula: PMT = P * [r(1 + r)^n] / [(1 + r)^n - 1]

        // p: Principal loan amount
        // r: Interest rate per period (e.g., annual rate / 12)
        // n: Total number of payment periods

        val one = BigDecimal.ONE

        val ratePlusOne = monthlyInterestRate.add(one)

        val compoundedInterest = ratePlusOne.pow(installments)

        val numerator = monthlyInterestRate.multiply(compoundedInterest)

        val denominator = compoundedInterest.subtract(one)

        val interestFactor = numerator.divide(denominator, 10, RoundingMode.HALF_UP)
        val rawInstallment = principal.multiply(interestFactor)

        return rawInstallment.setScale(2, RoundingMode.HALF_UP)
    }
}
