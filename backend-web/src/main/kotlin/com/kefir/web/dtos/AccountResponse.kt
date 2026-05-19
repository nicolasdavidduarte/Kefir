package com.kefir.web.dtos

import com.kefir.entities.Account
import java.math.BigDecimal

data class AccountResponse(
    val id: Long,
    val customer: Long,
    val currency: Long,
    val bank: Long,
    val cbu: String,
    val balance: BigDecimal,
    val status: Long,
)

fun Account.toResponse() = AccountResponse(
    id = this.id,
    customer = this.customer,
    currency = this.currency,
    bank = this.bank,
    cbu = this.cbu,
    balance = this.balance,
    status = this.status,
)
