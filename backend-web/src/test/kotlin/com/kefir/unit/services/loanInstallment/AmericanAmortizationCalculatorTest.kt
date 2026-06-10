package com.kefir.unit.services.loanInstallment

import com.kefir.services.loanInstallment.AmericanAmortizationCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal

class AmericanAmortizationCalculatorTest {

    private val americanCalculator = AmericanAmortizationCalculator()

    companion object {

        @JvmStatic
        fun americanScenarios() = listOf(
            Arguments.of(BigDecimal("6.25"), BigDecimal("100.00"), 4),
            Arguments.of(BigDecimal("6.25"), BigDecimal("5000.50"), 4),
            Arguments.of(BigDecimal("4.50"), BigDecimal("10000.00"), 12),
            Arguments.of(BigDecimal("2.00"), BigDecimal("25000.00"), 24),
        )
    }

    @Test
    fun calculateAmountFor4Installments() {
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
    fun calculateAmountForLowPrincipal() {
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
        assertEquals(BigDecimal("6.25"), schedule[1].interestAmount)
        assertEquals(BigDecimal("6.25"), schedule[1].totalAmount)
        assertEquals(BigDecimal("100.00"), schedule[1].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("0.00"), schedule[2].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[2].interestAmount)
        assertEquals(BigDecimal("6.25"), schedule[2].totalAmount)
        assertEquals(BigDecimal("100.00"), schedule[2].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("100.00"), schedule[3].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[3].interestAmount)
        assertEquals(BigDecimal("106.25"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }

    @ParameterizedTest
    @MethodSource("americanScenarios")
    fun principalIsPaidOnlyInLastInstallment(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        schedule.dropLast(1).forEach {
            assertEquals(
                BigDecimal("0.00"),
                it.principalAmount,
            )
        }

        assertEquals(
            principalAmount,
            schedule.last().principalAmount,
        )
    }

    @ParameterizedTest
    @MethodSource("americanScenarios")
    fun interestAmountIsConstant(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        val expected = schedule.first().interestAmount

        schedule.forEach {
            assertEquals(expected, it.interestAmount)
        }
    }

    @ParameterizedTest
    @MethodSource("americanScenarios")
    fun remainingBalanceDoesNotChangeUntilLastInstallment(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        val balance = principalAmount

        schedule.dropLast(1).forEach {
            assertEquals(balance, it.remainingBalance)
        }

        assertEquals(
            BigDecimal("0.00"),
            schedule.last().remainingBalance,
        )
    }

    @ParameterizedTest
    @MethodSource("americanScenarios")
    fun totalAmountIsConstantUntilLastInstallment(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = americanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        val expected = schedule.first().totalAmount

        schedule.dropLast(1).forEach {
            assertEquals(expected, it.totalAmount)
        }
    }
}
