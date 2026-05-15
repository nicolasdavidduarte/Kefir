package com.kefir.web.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ApprovalLogRequest(
    @field:NotBlank val entity: String?,
    @field:NotNull val approvableId: Long?,
    @field:NotNull val status: Long?,
    @field:NotBlank val comments: String?,
    @field:NotNull val actionedBy: Long?,
)
