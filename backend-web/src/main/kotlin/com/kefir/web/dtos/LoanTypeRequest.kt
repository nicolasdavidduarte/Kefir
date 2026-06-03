package com.kefir.web.dtos

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class LoanTypeRequest(
    @field:NotBlank(message = "Name is mandatory") val name: String,
    val description: String,
    @field:NotNull(value = "Base interest rate is mandatory") val baseInterestRate: BigDecimal,
)
