package com.kefir.web.DTOs

import org.jetbrains.annotations.NotNull

data class BankRequest(
    @field:NotNull("Bank name is required")
    val name: String?)
