package com.kefir.services.loanInstallment

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FrenchAmortizationCalculatorTest {

    @Test
    fun calculateInstallmentAmountForFrenchAmortizationType() {
        val frenchCalculator = FrenchAmortizationCalculator()

        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 4

        val schedule = frenchCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(schedule.size, 4)

        assertEquals(schedule[0].number, 1)
        assertEquals(schedule[0].principalAmount, BigDecimal("1138.84"))
        assertEquals(schedule[0].interestAmount, BigDecimal("312.53"))
        assertEquals(schedule[0].totalAmount, BigDecimal("1451.37"))
        assertEquals(schedule[0].remainingBalance, BigDecimal("3861.66"))
    }
}
