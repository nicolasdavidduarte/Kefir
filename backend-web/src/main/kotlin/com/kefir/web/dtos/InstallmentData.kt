package com.kefir.web.dtos

import java.math.BigDecimal

data class InstallmentData(
    val number: Int,
    val totalAmount: BigDecimal,
    val principalAmount: BigDecimal,
    val interestAmount: BigDecimal,
    val remainingBalance: BigDecimal,
)
