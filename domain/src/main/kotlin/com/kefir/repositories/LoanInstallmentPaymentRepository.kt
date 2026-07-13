package com.kefir.repositories

import com.kefir.entities.LoanInstallment
import com.kefir.entities.LoanInstallmentPayment
import org.springframework.data.jpa.repository.JpaRepository

interface LoanInstallmentPaymentRepository : JpaRepository<LoanInstallmentPayment, Long> {
    fun findByLoanInstallment(installmentId: LoanInstallment): LoanInstallmentPayment
}
