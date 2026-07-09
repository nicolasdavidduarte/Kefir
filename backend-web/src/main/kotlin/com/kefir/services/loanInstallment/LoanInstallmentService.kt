package com.kefir.services.loanInstallment

import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.enums.AmortizationTypeName
import com.kefir.enums.LoanInstallmentStatus
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.LoanInstallmentRepository
import com.kefir.services.UserService
import com.kefir.web.dtos.LoanInstallmentResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class LoanInstallmentService(
    private val loanInstallmentRepository: LoanInstallmentRepository,
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
        val loanInstallment = loanInstallmentRepository.findByLoanIdAndNumber(loanId, installmentNumber)

        loanInstallment.status = LoanInstallmentStatus.PAID
        loanInstallment.updatedAt = OffsetDateTime.now()
        loanInstallment.user = userService.getById(authService.currentUserId)

        return loanInstallmentRepository.save(loanInstallment).toResponse()
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
                loan.user,
            )
        }

        return loanInstallmentRepository.saveAll(loanInstallments)
    }
}
