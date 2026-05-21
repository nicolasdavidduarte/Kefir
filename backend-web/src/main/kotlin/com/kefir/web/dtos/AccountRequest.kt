package com.kefir.web.dtos

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AccountRequest(
    @field:NotNull("Type is required")
    val type: Long?,
    @field:NotNull("Customer is required")
    val customer: Long?,
    @field:NotNull("Currency is required")
    val currency: Long?,
    @field:NotNull("Bank is required")
    val bank: Long?,
    @field:NotNull("Bank branch is required")
    val bankBranch: Long?,
    val initialBalance: BigDecimal = BigDecimal.ZERO,
)
