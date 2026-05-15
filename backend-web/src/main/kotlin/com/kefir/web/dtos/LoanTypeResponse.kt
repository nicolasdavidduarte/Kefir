package com.kefir.web.dtos

import com.kefir.entities.LoanType

data class LoanTypeResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val status: Int,
)

fun LoanType.toResponse() = LoanTypeResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    status = this.status,
)
