package com.kefir.services.loanInstallment.payment

import com.kefir.entities.PaymentMethod
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.PaymentMethodRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentMethodService(
    private val paymentMethodRepository: PaymentMethodRepository,
) {

    @Transactional
    fun getByName(name: String): PaymentMethod = paymentMethodRepository.findByName(name).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_VALID) }
}
