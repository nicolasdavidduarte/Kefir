package com.kefir.unit.services.loanInstallment

import com.kefir.services.loanInstallment.FrenchAmortizationCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FrenchAmortizationCalculatorTest {

    @Test
    fun calculateAmountFor4Installments() {
        val frenchCalculator = FrenchAmortizationCalculator()

        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 4

        val schedule = frenchCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(schedule.size, 4)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("1138.84"), schedule[0].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[0].interestAmount)
        assertEquals(BigDecimal("1451.37"), schedule[0].totalAmount)
        assertEquals(BigDecimal("3861.66"), schedule[0].remainingBalance)

        // Second installment
        assertEquals(2, schedule[1].number)
        assertEquals(BigDecimal("1210.02"), schedule[1].principalAmount)
        assertEquals(BigDecimal("241.35"), schedule[1].interestAmount)
        assertEquals(BigDecimal("1451.37"), schedule[1].totalAmount)
        assertEquals(BigDecimal("2651.64"), schedule[1].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("1285.64"), schedule[2].principalAmount)
        assertEquals(BigDecimal("165.73"), schedule[2].interestAmount)
        assertEquals(BigDecimal("1451.37"), schedule[2].totalAmount)
        assertEquals(BigDecimal("1366.00"), schedule[2].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("1366.00"), schedule[3].principalAmount)
        assertEquals(BigDecimal("85.38"), schedule[3].interestAmount)
        assertEquals(BigDecimal("1451.37"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }

    @Test
    fun calculateAmountForLowPrincipal() {
        val frenchCalculator = FrenchAmortizationCalculator()

        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("100.00")
        val numberOfInstallments = 4

        val schedule = frenchCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("22.77"), schedule[0].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].interestAmount)
        assertEquals(BigDecimal("29.02"), schedule[0].totalAmount)
        assertEquals(BigDecimal("77.23"), schedule[0].remainingBalance)

        // Second installment
        assertEquals(2, schedule[1].number)
        assertEquals(BigDecimal("24.19"), schedule[1].principalAmount)
        assertEquals(BigDecimal("4.83"), schedule[1].interestAmount)
        assertEquals(BigDecimal("29.02"), schedule[1].totalAmount)
        assertEquals(BigDecimal("53.04"), schedule[1].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("25.70"), schedule[2].principalAmount)
        assertEquals(BigDecimal("3.32"), schedule[2].interestAmount)
        assertEquals(BigDecimal("29.02"), schedule[2].totalAmount)
        assertEquals(BigDecimal("27.34"), schedule[2].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("27.34"), schedule[3].principalAmount)
        assertEquals(BigDecimal("1.71"), schedule[3].interestAmount)
        assertEquals(BigDecimal("29.02"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }
}
