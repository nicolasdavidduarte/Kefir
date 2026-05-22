package com.kefir.web.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OperationLogCommand(
    @field:NotBlank val entity: String?,
    @field:NotNull val entityId: Long?,
    @field:NotBlank val operation: String?,
    @field:NotBlank val comments: String?,
)
