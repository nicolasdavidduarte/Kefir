package com.kefir.unit.services.loanInstallment

import com.kefir.services.loanInstallment.GermanAmortizationCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal

class GermanAmortizationCalculatorTest {

    private val germanCalculator = GermanAmortizationCalculator()

    companion object {

        @JvmStatic
        fun germanScenarios() = listOf(
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

        val schedule = germanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(schedule.size, 4)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("1250.13"), schedule[0].principalAmount)
        assertEquals(BigDecimal("312.53"), schedule[0].interestAmount)
        assertEquals(BigDecimal("1562.66"), schedule[0].totalAmount)
        assertEquals(BigDecimal("3750.37"), schedule[0].remainingBalance)

        // Second installment
        assertEquals(2, schedule[1].number)
        assertEquals(BigDecimal("1250.13"), schedule[1].principalAmount)
        assertEquals(BigDecimal("234.40"), schedule[1].interestAmount)
        assertEquals(BigDecimal("1484.53"), schedule[1].totalAmount)
        assertEquals(BigDecimal("2500.24"), schedule[1].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("1250.13"), schedule[2].principalAmount)
        assertEquals(BigDecimal("156.27"), schedule[2].interestAmount)
        assertEquals(BigDecimal("1406.40"), schedule[2].totalAmount)
        assertEquals(BigDecimal("1250.11"), schedule[2].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("1250.11"), schedule[3].principalAmount)
        assertEquals(BigDecimal("78.13"), schedule[3].interestAmount)
        assertEquals(BigDecimal("1328.24"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }

    @Test
    fun calculateAmountForLowPrincipal() {
        val monthlyInterestRate = BigDecimal("6.25")
        val principalAmount = BigDecimal("100.00")
        val numberOfInstallments = 4

        val schedule = germanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        // First installment
        assertEquals(1, schedule[0].number)
        assertEquals(BigDecimal("25.00"), schedule[0].principalAmount)
        assertEquals(BigDecimal("6.25"), schedule[0].interestAmount)
        assertEquals(BigDecimal("31.25"), schedule[0].totalAmount)
        assertEquals(BigDecimal("75.00"), schedule[0].remainingBalance)

        // Second installment
        assertEquals(2, schedule[1].number)
        assertEquals(BigDecimal("25.00"), schedule[1].principalAmount)
        assertEquals(BigDecimal("4.69"), schedule[1].interestAmount)
        assertEquals(BigDecimal("29.69"), schedule[1].totalAmount)
        assertEquals(BigDecimal("50.00"), schedule[1].remainingBalance)

        // Third installment
        assertEquals(3, schedule[2].number)
        assertEquals(BigDecimal("25.00"), schedule[2].principalAmount)
        assertEquals(BigDecimal("3.13"), schedule[2].interestAmount)
        assertEquals(BigDecimal("28.13"), schedule[2].totalAmount)
        assertEquals(BigDecimal("25.00"), schedule[2].remainingBalance)

        // Fourth installment
        assertEquals(4, schedule[3].number)
        assertEquals(BigDecimal("25.00"), schedule[3].principalAmount)
        assertEquals(BigDecimal("1.56"), schedule[3].interestAmount)
        assertEquals(BigDecimal("26.56"), schedule[3].totalAmount)
        assertEquals(BigDecimal("0.00"), schedule[3].remainingBalance)
    }

    @ParameterizedTest
    @MethodSource("germanScenarios")
    fun principalAmountIsConstantUntilLastInstallment(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = germanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        assertEquals(
            schedule[0].principalAmount,
            schedule[1].principalAmount,
        )

        assertEquals(
            schedule[1].principalAmount,
            schedule[2].principalAmount,
        )
    }

    @ParameterizedTest
    @MethodSource("germanScenarios")
    fun interestAmountsDecreaseEveryInstallment(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = germanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        for (i in 1..<schedule.size) {
            assert(
                schedule[i].interestAmount <
                    schedule[i - 1].interestAmount,
            )
        }
    }

    @ParameterizedTest
    @MethodSource("germanScenarios")
    fun totalAmountsDecreaseEveryInstallment(
        monthlyInterestRate: BigDecimal,
        principalAmount: BigDecimal,
        numberOfInstallments: Int,
    ) {
        val schedule = germanCalculator.generateSchedule(monthlyInterestRate, principalAmount, numberOfInstallments)

        for (i in 1..<schedule.size) {
            assert(schedule[i].totalAmount < schedule[i - 1].totalAmount)
        }
    }
}
