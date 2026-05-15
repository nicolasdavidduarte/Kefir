package com.kefir.web.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class EntityApprovalRequest(
    @field:JsonProperty("approve")
    @field:NotNull("Approve field is required (boolean)")
    val approve: Boolean?,
    @field:NotBlank(message = "Comments cannot be empty")
    val comments: String?,
)
