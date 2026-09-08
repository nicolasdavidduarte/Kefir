package com.kefir.repositories

import com.kefir.entities.OperationLog
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OperationLogRepository : JpaRepository<OperationLog, Long> {
    fun findByEntityAndEntityIdAndOperation(entity: String, entityId: Long, operationLog: String): Optional<OperationLog>
}
