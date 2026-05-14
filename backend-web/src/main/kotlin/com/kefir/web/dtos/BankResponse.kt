package com.kefir.web.dtos

import com.kefir.entities.Bank
import java.time.OffsetDateTime

data class BankResponse(
    val id: Long,
    val name: String,
    val status: Long,
    val createdAt: OffsetDateTime,
)

fun Bank.toResponse() =
    BankResponse(
        id = this.id,
        name = this.name,
        status = this.status,
        createdAt = this.createdAt,
    )
