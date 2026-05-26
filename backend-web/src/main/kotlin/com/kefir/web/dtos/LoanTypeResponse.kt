package com.kefir.web.dtos

import com.kefir.entities.LoanType

data class LoanTypeResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val enabled: Boolean,
)

fun LoanType.toResponse() = LoanTypeResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    enabled = this.enabled,
)
