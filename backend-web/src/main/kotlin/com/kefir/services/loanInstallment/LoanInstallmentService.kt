package com.kefir.services.loanInstallment

import com.kefir.entities.Account
import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.enums.AmortizationTypeName
import com.kefir.enums.LoanInstallmentStatus
import com.kefir.enums.LoanStatus
import com.kefir.enums.PaymentMethodName
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.LoanInstallmentRepository
import com.kefir.repositories.LoanRepository
import com.kefir.services.UserService
import com.kefir.services.account.AccountService
import com.kefir.services.loanInstallment.payment.LoanInstallmentPaymentService
import com.kefir.services.loanInstallment.payment.PaymentMethodService
import com.kefir.web.dtos.LoanInstallmentResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class LoanInstallmentService(
    private val loanRepository: LoanRepository,
    private val loanInstallmentRepository: LoanInstallmentRepository,
    private val paymentMethodService: PaymentMethodService,
    private val loanInstallmentPaymentService: LoanInstallmentPaymentService,
    private val accountService: AccountService,
    private val userService: UserService,
    private val authService: AuthService,
    calculatorsList: List<AmortizationCalculator>,
) {

    private val calculators: Map<AmortizationTypeName, AmortizationCalculator> =
        calculatorsList.associateBy { it.type }

    @Transactional(readOnly = true)
    fun getAllInstallments(loanId: Long): List<LoanInstallmentResponse> {
        val loanInstallments = loanInstallmentRepository.findAllByLoanIdOrderByNumberAsc(loanId).map(LoanInstallment::toResponse)

        if (loanInstallments.isEmpty()) {
            throw ApiException(ErrorCode.LOAN_INSTALLMENTS_NOT_FOUND)
        }

        return loanInstallments
    }

    fun payInstallment(loanId: Long, installmentNumber: Int): LoanInstallmentResponse {
        val paymentSchedule = loanInstallmentRepository.findAllByLoanIdOrderByNumberAsc(loanId)

        val currentInstallment = paymentSchedule[installmentNumber - 1]

        val previousInstallment: LoanInstallment? = if (installmentNumber > 1) {
            paymentSchedule[installmentNumber - 2]
        } else {
            null
        }

        // TODO: Add payment method from a PaymentRequest
        val paymentMethod = paymentMethodService.getByName(PaymentMethodName.HOMEBANKING.name)

        val account = currentInstallment.loan.account

        paymentValidations(currentInstallment, previousInstallment, account)

        accountService.subtractBalance(account, currentInstallment.totalAmount)

        val user = userService.getById(authService.currentUserId)

        currentInstallment.status = LoanInstallmentStatus.PAID
        currentInstallment.updatedAt = OffsetDateTime.now()
        currentInstallment.createdBy = user
        currentInstallment.updatedBy = user

        loanInstallmentPaymentService.create(currentInstallment, paymentMethod)

        if (currentInstallment.number == paymentSchedule.last().number) {
            val loan = currentInstallment.loan

            loan.status = LoanStatus.CLOSED
            loan.updatedBy = user
            loan.updatedAt = OffsetDateTime.now()

            loanRepository.save(loan)
        }

        return loanInstallmentRepository.save(currentInstallment).toResponse()
    }

    fun paymentValidations(loanInstallment: LoanInstallment, previousInstallment: LoanInstallment?, account: Account) {
        if (loanInstallment.loan.status != LoanStatus.ACTIVE) {
            throw ApiException(ErrorCode.LOAN_NOT_VALID)
        }

        if (account.balance.subtract(loanInstallment.totalAmount) < BigDecimal.ZERO) {
            throw ApiException(ErrorCode.ACCOUNT_WITHOUT_FUNDS)
        }

        if (loanInstallment.status != LoanInstallmentStatus.PAYMENT_PENDING && loanInstallment.status != LoanInstallmentStatus.OVERDUE) {
            throw ApiException(ErrorCode.LOAN_INSTALLMENT_NOT_VALID_FOR_PAYMENT)
        }

        if (previousInstallment != null) {
            if (previousInstallment.status != LoanInstallmentStatus.PAID) {
                throw ApiException(ErrorCode.LOAN_INSTALLMENT_PENDING)
            }
        }
    }

    @Transactional
    fun createInstallmentsSchedule(loan: Loan): List<LoanInstallment> {
        val calculator = calculators[loan.amortizationType.name]
            ?: throw IllegalArgumentException("Unsupported amortization type: ${loan.amortizationType.name}")

        val schedule = calculator.generateSchedule(
            loan.monthlyInterestRate,
            loan.principalAmount,
            loan.numberOfInstallments,
        )

        val loanInstallments = schedule.map { i ->
            LoanInstallment.createNew(
                loan,
                i.number,
                i.principalAmount,
                i.interestAmount,
                i.totalAmount,
                i.remainingBalance,
                loan.openingDate.plusMonths(i.number.toLong()),
                loan.createdBy,
            )
        }

        return loanInstallmentRepository.saveAll(loanInstallments)
    }

    fun updateInstallmentsForChargeOff(loanId: Long) {
        val installments = loanInstallmentRepository.findByLoanIdForChargeOff(loanId)

        installments.forEach {
            it.status = LoanInstallmentStatus.CHARGE_OFF
            it.updatedAt = OffsetDateTime.now()
        }

        loanInstallmentRepository.saveAll(installments)
    }
}
