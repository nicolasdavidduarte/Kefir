package com.kefir.web.dtos.loanInstallmentPayment

import com.kefir.enums.PaymentMethodName

data class LoanInstallmentPaymentRequest(

    val paymentMethod: PaymentMethodName,

)
