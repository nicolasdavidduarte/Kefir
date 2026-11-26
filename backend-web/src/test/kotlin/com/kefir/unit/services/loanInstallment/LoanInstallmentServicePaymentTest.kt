package com.kefir.unit.services.loanInstallment

import com.kefir.entities.Account
import com.kefir.entities.AmortizationType
import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.entities.LoanType
import com.kefir.entities.PaymentMethod
import com.kefir.entities.User
import com.kefir.enums.AmortizationTypeName
import com.kefir.enums.LoanInstallmentStatus
import com.kefir.enums.LoanStatus
import com.kefir.enums.PaymentMethodName
import com.kefir.infrastructure.security.AuthService
import com.kefir.persistence.repositories.LoanInstallmentRepository
import com.kefir.persistence.repositories.LoanRepository
import com.kefir.services.account.AccountCommandService
import com.kefir.services.loanInstallment.GermanAmortizationCalculator
import com.kefir.services.loanInstallment.LoanInstallmentService
import com.kefir.services.loanInstallment.payment.LoanInstallmentPaymentService
import com.kefir.services.loanInstallment.payment.PaymentMethodService
import com.kefir.services.user.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Optional

class LoanInstallmentServicePaymentTest {

    private val loanRepository: LoanRepository = mockk()
    private val loanInstallmentRepository: LoanInstallmentRepository = mockk()
    private val paymentMethodService: PaymentMethodService = mockk()
    private val loanInstallmentPaymentService: LoanInstallmentPaymentService = mockk()
    private val accountCommandService: AccountCommandService = mockk()
    private val userService: UserService = mockk()
    private val authService: AuthService = mockk()

    private val germanCalculator = GermanAmortizationCalculator()

    private val service = LoanInstallmentService(
        loanRepository = loanRepository,
        loanInstallmentRepository = loanInstallmentRepository,
        paymentMethodService = paymentMethodService,
        loanInstallmentPaymentService = loanInstallmentPaymentService,
        accountCommandService = accountCommandService,
        userService = userService,
        authService = authService,
        calculatorsList = listOf(germanCalculator),
    )

    private val principal = BigDecimal("100.001")
    private val numberOfInstallments = 3
    private val userId = 1
    private val loanId = 1L

    private lateinit var user: User
    private lateinit var account: Account
    private lateinit var amortizationType: AmortizationType
    private lateinit var loan: Loan
    private lateinit var installments: List<LoanInstallment>
    private lateinit var paymentMethod: PaymentMethod

    @BeforeEach
    fun setUp() {
        user = mockk(relaxed = true)
        account = mockk(relaxed = true)
        paymentMethod = mockk(relaxed = true)

        amortizationType = mockk {
            every { name } returns AmortizationTypeName.GERMAN
        }

        loan = Loan.builder()
            .id(loanId)
            .customer(mockk(relaxed = true))
            .account(account)
            .loanType(mockk<LoanType>(relaxed = true))
            .amortizationType(amortizationType)
            .numberOfInstallments(numberOfInstallments)
            .currency(mockk(relaxed = true))
            .annualInterestRate(BigDecimal("12.00"))
            .monthlyInterestRate(BigDecimal("1.00"))
            .principalAmount(principal)
            .interestAmount(BigDecimal.ZERO)
            .totalOperationAmount(BigDecimal.ZERO)
            .remainingBalance(principal)
            .openingDate(OffsetDateTime.now())
            .expirationDate(OffsetDateTime.now().plusMonths(numberOfInstallments.toLong()))
            .accruedInterest(BigDecimal.ZERO)
            .status(LoanStatus.ACTIVE)
            .createdBy(user)
            .createdAt(OffsetDateTime.now())
            .updatedBy(user)
            .updatedAt(OffsetDateTime.now())
            .build()

        val schedule = germanCalculator.generateSchedule(
            BigDecimal("1.00"),
            principal,
            numberOfInstallments,
        )

        installments = schedule.mapIndexed { index, data ->
            LoanInstallment.createNew(
                loan = loan,
                number = data.number,
                principalAmount = data.principalAmount,
                interestAmount = data.interestAmount,
                totalAmount = data.totalAmount,
                remainingBalance = data.remainingBalance,
                paymentDueDate = OffsetDateTime.now().plusMonths((index + 1).toLong()),
                createdBy = user,
            )
        }

        every { authService.currentUserId } returns userId
        every { userService.getById(userId) } returns user
        every { accountCommandService.getByLoanIdForUpdate(loanId) } returns account
        every { loanRepository.findByIdForUpdate(loanId) } returns Optional.of(loan)
        every { paymentMethodService.getByName(PaymentMethodName.CASH.name) } returns paymentMethod
        every { loanInstallmentPaymentService.create(any(), any()) } returns mockk(relaxed = true)
        every { account.subtractBalance(any(), any()) } returns Unit
    }

    @Test
    fun afterPayOffLoanRemainingBalanceShouldBeZero() {
        for (installmentNumber in 1..numberOfInstallments) {
            // Return the full schedule for every call so previous-installment look-up works
            every { loanInstallmentRepository.findAllByLoanIdOrderByNumberAsc(loanId) } returns installments

            // Mark previous installment as PAID so the sequential validation passes
            if (installmentNumber > 1) {
                installments[installmentNumber - 2].status = LoanInstallmentStatus.PAID
            }

            service.createPayment(loanId, installmentNumber, PaymentMethodName.CASH)
        }

        assertEquals(
            0,
            BigDecimal.ZERO.compareTo(loan.remainingBalance),
            "Expected loan.remainingBalance to be ZERO after full payoff, " +
                "but was ${loan.remainingBalance}",
        )
    }
}
