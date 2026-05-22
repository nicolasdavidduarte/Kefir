package com.kefir.web.dtos

import java.time.LocalDateTime

data class EntityOperationResponse(
    val operation: String,
    val entity: String,
    val id: Long,
    val message: String,
    val timestamp: LocalDateTime,
)
