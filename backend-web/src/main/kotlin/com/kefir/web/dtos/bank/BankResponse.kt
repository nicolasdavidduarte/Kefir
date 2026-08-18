package com.kefir.web.dtos.bank

import com.kefir.entities.Bank
import com.kefir.enums.BankStatus
import java.time.OffsetDateTime

data class BankResponse(
    val id: Int,
    val name: String,
    val status: BankStatus,
    val createdAt: OffsetDateTime,
)

fun Bank.toResponse() = BankResponse(
    id = this.id,
    name = this.name,
    status = this.status,
    createdAt = this.createdAt,
)
