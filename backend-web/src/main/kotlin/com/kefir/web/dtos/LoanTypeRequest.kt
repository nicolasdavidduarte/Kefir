package com.kefir.web.dtos

import jakarta.validation.constraints.NotBlank

data class LoanTypeRequest(
    @field:NotBlank(message = "Name is mandatory") val name: String,
    val description: String,
)
