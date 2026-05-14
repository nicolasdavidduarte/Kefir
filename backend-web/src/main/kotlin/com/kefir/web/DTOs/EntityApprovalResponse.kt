package com.kefir.web.DTOs

import java.time.LocalDateTime

data class EntityApprovalResponse(
    val message: String, val entity: String, val id: Long, val timestamp: LocalDateTime
)
