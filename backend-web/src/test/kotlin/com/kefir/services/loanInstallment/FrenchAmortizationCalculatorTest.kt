package com.kefir.services.loanInstallment

import org.junit.jupiter.api.Test

class FrenchAmortizationCalculatorTest {

    @Test
    fun calculateInstallmentAmountForFrenchAmortizationType() {
//        val frenchCalculator = FrenchAmortizationCalculator()
//
//        val now = OffsetDateTime.now()
//
//        val customerType = com.kefir.entities.CustomerType()
//
//        val documentType = com.kefir.entities.DocumentType()
//
//        val user = UserService()
//
//        val customer = Customer.builder()
//            .id(1L)
//            .name1("Carlos")
//            .lastname1("Alcaraz")
//            .customerType(customerType)
//            .status(CustomerStatus.ACTIVE)
//            .createdAt(now)
//            .updatedAt(now)
//            .documentType(documentType)
//            .documentNumber("5425434")
//            .user(user)
//            .build()
//
//        val currency = currencyService.getByIsoCode(CurrencyIsoCodes.USD)
//        val loanType = loanTypeService.getByNameIgnoringCase(LoanTypeName.PERSONAL)
//        val amortizationType = amortizationTypeService.getByNameIgnoringCase(AmortizationTypeName.FRENCH)
//
//        val principalAmount = BigDecimal("5000.50")
//        val numberOfInstalments = 4
//
//        val annualInterestRate = BigDecimal("75")
//        val monthlyInterestRate = annualInterestRate.divide(BigDecimal("12"))
//
//        val loan = Loan.builder()
//            .id(1L)
//            .customer(customer)
//            .currency(currency)
//            .loanType(loanType)
//            .amortizationType(amortizationType)
//            .numberOfInstallments(numberOfInstalments)
//            .annualInterestRate(annualInterestRate)
//            .monthlyInterestRate(monthlyInterestRate)
//            .user(user)
//            .status(LoanStatus.PENDING)
//            .principalAmount(principalAmount)
//            .openingDate(now)
//            .expirationDate(now.plusMonths(numberOfInstalments.toLong()))
//            .externalId(9999)
//            .updatedAt(now)
//            .createdAt(now)
//            .build()
//
//        val schedule = frenchCalculator.generateSchedule(loan)
//
//        assertEquals(schedule.size, 4)
//
//        assertEquals(schedule[0].number, 1)
//        assertEquals(schedule[0].principalAmount, BigDecimal("1138.8400"))
//        assertEquals(schedule[0].interestAmount, BigDecimal("312.5300"))
//        assertEquals(schedule[0].totalAmount, BigDecimal("1451.3700"))
//        assertEquals(schedule[0].remainingBalance, BigDecimal("3861.6600"))
    }
}
