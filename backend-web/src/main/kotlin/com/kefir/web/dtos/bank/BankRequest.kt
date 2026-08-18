package com.kefir.web.dtos.bank

import org.jetbrains.annotations.NotNull

data class BankRequest(
    @field:NotNull("Bank name is required")
    val name: String?,
)
