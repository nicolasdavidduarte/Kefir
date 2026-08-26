package com.kefir.web.dtos.account

import com.kefir.entities.Account
import com.kefir.enums.AccountStatus
import java.math.BigDecimal
import java.time.OffsetDateTime

data class AccountResponse(
    val id: Long,
    val customer: String,
    val type: String,
    val currencyIsoCode: String,
    val bank: String,
    val accountNumber: String,
    val cbu: String,
    val balance: BigDecimal?,
    val status: AccountStatus,
    val createdBy: String,
    val createdAt: OffsetDateTime,
    val updatedBy: String,
    val updatedAt: OffsetDateTime,
)

fun Account.toResponse() = AccountResponse(
    id = this.id,
    customer = this.customer.fullname,
    type = this.type.name,
    currencyIsoCode = this.currency.isoCode,
    bank = this.bank.name,
    accountNumber = this.accountNumber,
    cbu = this.cbu,
    balance = this.balance,
    status = this.status,
    createdBy = this.createdBy.username,
    createdAt = this.createdAt,
    updatedBy = this.updatedBy.username,
    updatedAt = this.updatedAt,
)
