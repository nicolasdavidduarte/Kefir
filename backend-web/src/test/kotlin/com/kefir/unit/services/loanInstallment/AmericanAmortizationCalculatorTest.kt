package com.kefir.unit.services.loanInstallment

import com.kefir.exceptions.ApiException
import com.kefir.services.loanInstallment.AmericanAmortizationCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class AmericanAmortizationCalculatorTest {

    @Test
    fun calculateAmountFor4Installments() {
        val americanCalculator = AmericanAmortizationCalculator()

        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 4

        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(schedule.size, 4)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("0.00"), schedule[0].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[0].interestAmount)
        assertEquals(BigDecimal("312.53"), schedule[0].totalAmount)
        assertEquals(BigDecimal("5000.50"), schedule[0].remainingBalance)

        // Second installment
        assertEquals(2, schedule[1].number)
        assertEquals(BigDecimal("0.00"), schedule[1].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[1].interestAmount)
        assertEquals(BigDecimal("312.53"), schedule[1].totalAmount)
        assertEquals(BigDecimal("5000.50"), schedule[1].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("0.00"), schedule[2].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[2].interestAmount)
        assertEquals(BigDecimal("312.53"), schedule[2].totalAmount)
        assertEquals(BigDecimal("5000.50"), schedule[2].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("5000.50"), schedule[3].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[3].interestAmount)
        assertEquals(BigDecimal("5313.03"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }

    @Test
    fun calculateAmountFor1Installment() {
        val americanCalculator = AmericanAmortizationCalculator()

        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 1

        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(schedule.size, 1)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("5000.50"), schedule[0].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[0].interestAmount)
        assertEquals(BigDecimal("5313.03"), schedule[0].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[0].remainingBalance)
    }

    @Test
    fun calculateAmountWithInterestRateZero() {
        val americanCalculator = AmericanAmortizationCalculator()

        val monthlyInterestRate = BigDecimal.ZERO
        val principalAmount = BigDecimal("5000.50")
        val numberOfInstallments = 4

        assertThrows<ApiException> {
            americanCalculator.generateSchedule(
                monthlyInterestRate,
                principalAmount,
                numberOfInstallments,
            )
        }
    }

    @Test
    fun calculateAmountForLowPrincipal() {
        val americanCalculator = AmericanAmortizationCalculator()

        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("100.00")
        val numberOfInstallments = 4

        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("0.00"), schedule[0].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].interestAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].totalAmount)
        assertEquals(BigDecimal("100.00"), schedule[0].remainingBalance)

        // Second installment
        assertEquals(2, schedule[1].number)
        assertEquals(BigDecimal("0.00"), schedule[1].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].interestAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].totalAmount)
        assertEquals(BigDecimal("100.00"), schedule[0].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("0.00"), schedule[2].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].interestAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].totalAmount)
        assertEquals(BigDecimal("100.00"), schedule[0].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("100.00"), schedule[3].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].interestAmount)
        assertEquals(BigDecimal("106.25"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }
}
