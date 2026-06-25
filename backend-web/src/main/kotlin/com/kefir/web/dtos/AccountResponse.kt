package com.kefir.web.dtos

import com.kefir.entities.Account
import com.kefir.enums.AccountStatus
import java.math.BigDecimal

data class AccountResponse(
    val id: Long,
    val customer: String,
    val type: String,
    val currencyIsoCode: String,
    val bank: String,
    val cbu: String,
    val balance: BigDecimal?,
    val status: AccountStatus,
)

fun Account.toResponse() = AccountResponse(
    id = this.id,
    customer = this.customer.fullname,
    type = this.type.name,
    currencyIsoCode = this.currency.isoCode,
    bank = this.bank.name,
    cbu = this.cbu,
    balance = this.balance,
    status = this.status,
)
