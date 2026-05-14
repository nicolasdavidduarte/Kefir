package com.kefir.web.DTOs

import com.kefir.entities.Account
import java.math.BigDecimal

data class AccountResponse(val id: Long,
                           val customer: Long,
                           val currency: Long,
                           val bank: Long,
                           val CBU: String,
                           val balance: BigDecimal,
                           val status: Int)

fun Account.toResponse() = AccountResponse(id = this.id, customer = this.customer, currency = this.currency, bank = this.bank, CBU = this.CBU, balance = this.balance, status = this.status)