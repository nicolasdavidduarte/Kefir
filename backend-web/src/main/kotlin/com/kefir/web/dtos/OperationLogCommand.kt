package com.kefir.web.dtos

import com.kefir.enums.EntityName
import com.kefir.enums.LogOperation
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OperationLogCommand(
    @field:NotBlank val operation: LogOperation,
    @field:NotBlank val entity: EntityName,
    @field:NotNull val entityId: Long?,
    @field:NotBlank val comments: String?,
)
