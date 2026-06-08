package com.kefir.unit.services.loanInstallment

import com.kefir.exceptions.ApiException
import com.kefir.services.loanInstallment.AmericanAmortizationCalculator
import com.kefir.services.loanInstallment.AmortizationCalculator
import com.kefir.services.loanInstallment.FrenchAmortizationCalculator
import com.kefir.services.loanInstallment.GermanAmortizationCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.util.stream.Stream

class AmortizationCalculatorTest {

    companion object {

        @JvmStatic
        fun calculators(): Stream<AmortizationCalculator> = Stream.of(
            FrenchAmortizationCalculator(),
            AmericanAmortizationCalculator(),
            GermanAmortizationCalculator(),
        )
    }

    @ParameterizedTest
    @MethodSource("calculators")
    fun calculateAmountFor1Installment(
        calculator: AmortizationCalculator,
    ) {
        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 1

        val schedule = calculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(schedule.size, 1)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("5000.50"), schedule[0].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[0].interestAmount)
        assertEquals(BigDecimal("5313.03"), schedule[0].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[0].remainingBalance)
    }

    @ParameterizedTest
    @MethodSource("calculators")
    fun calculateAmountWithInterestRateZero(
        calculator: AmortizationCalculator,
    ) {
        val monthlyInterestRate = BigDecimal.ZERO
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 4

        assertThrows<ApiException> {
            calculator.generateSchedule(
                monthlyInterestRate,
                principalAmount,
                numberOfInstallments,
            )
        }
    }
}
