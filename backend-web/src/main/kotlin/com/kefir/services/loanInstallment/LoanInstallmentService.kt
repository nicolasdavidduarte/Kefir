package com.kefir.services

import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.enums.AmortizationTypeName
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.LoanInstallmentRepository
import com.kefir.services.loanInstallment.AmortizationCalculator
import com.kefir.web.dtos.LoanInstallmentResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service

@Service
class LoanInstallmentService(
    private val loanInstallmentRepository: LoanInstallmentRepository,
    calculatorsList: List<AmortizationCalculator>,
) {

    private val calculators: Map<AmortizationTypeName, AmortizationCalculator> =
        calculatorsList.associateBy { it.type }

    fun getAllInstallments(): List<LoanInstallmentResponse> {
        val loanInstallments = loanInstallmentRepository.findAllByOrderByNumberAsc().map(LoanInstallment::toResponse)

        if (loanInstallments.isEmpty()) {
            throw ApiException(ErrorCode.LOAN_INSTALLMENTS_NOT_FOUND)
        }

        return loanInstallments
    }

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
