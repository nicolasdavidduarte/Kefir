package com.kefir.web.dtos

import com.kefir.entities.Account
import com.kefir.enums.AccountStatus
import java.math.BigDecimal

data class AccountResponse(
    val id: Long,
    val customer: Long,
    val type: String,
    val currencyIsoCode: String,
    val bank: Int,
    val cbu: String,
    val balance: BigDecimal?,
    val status: AccountStatus,
)

fun Account.toResponse() = AccountResponse(
    id = this.id,
    customer = this.customer.id,
    type = this.type.name,
    currencyIsoCode = this.currency.isoCode,
    bank = this.bank.id,
    cbu = this.cbu,
    balance = this.balance,
    status = this.status,
)
