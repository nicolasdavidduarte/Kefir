package com.kefir.services.loanInstallment.payment

import com.kefir.entities.Account
import com.kefir.entities.Loan
import com.kefir.entities.LoanInstallment
import com.kefir.entities.LoanInstallmentPayment
import com.kefir.entities.PaymentMethod
import com.kefir.enums.AmortizationTypeName
import com.kefir.enums.LoanInstallmentStatus
import com.kefir.enums.LoanStatus
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.LoanInstallmentPaymentRepository
import com.kefir.repositories.LoanInstallmentRepository
import com.kefir.repositories.PaymentMethodRepository
import com.kefir.services.UserService
import com.kefir.services.account.AccountService
import com.kefir.services.loanInstallment.AmortizationCalculator
import com.kefir.web.dtos.LoanInstallmentResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class PaymentMethodService(
    private val paymentMethodRepository: PaymentMethodRepository
) {

    @Transactional
    fun getByName(name: String): PaymentMethod {

        return paymentMethodRepository.findByName(name).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_VALID) }

    }

}
