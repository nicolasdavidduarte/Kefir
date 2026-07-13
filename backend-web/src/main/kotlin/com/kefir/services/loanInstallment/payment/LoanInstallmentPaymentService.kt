package com.kefir.services.loanInstallment.payment

import com.kefir.entities.LoanInstallment
import com.kefir.entities.LoanInstallmentPayment
import com.kefir.entities.PaymentMethod
import com.kefir.repositories.LoanInstallmentPaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class LoanInstallmentPaymentService(
    private val loanInstallmentPaymentRepository: LoanInstallmentPaymentRepository,
) {

    @Transactional
    fun create(installment: LoanInstallment, paymentMethod: PaymentMethod): LoanInstallmentPayment = loanInstallmentPaymentRepository.save(
        LoanInstallmentPayment(
            loanInstallment = installment,
            amountPaid = installment.totalAmount,
            paymentDate = OffsetDateTime.now(),
            paymentMethod = paymentMethod,
            createdBy = installment.createdBy,
            updatedBy = installment.updatedBy,
        ),
    )
}
