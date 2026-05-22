package com.kefir.services

import com.kefir.entities.OperationLog
import com.kefir.repositories.OperationLogRepository
import com.kefir.web.dtos.OperationLogCommand
import org.springframework.stereotype.Service

@Service
class OperationLogService(
    val operationLogRepository: OperationLogRepository,
    val auxAuthService: AuxAuthService,
) {
    fun create(operationLogCommand: OperationLogCommand) {
        val user: Int = auxAuthService.retrieveUserIdFromAuth()

        val operationLog =
            OperationLog(
                entity = requireNotNull(operationLogCommand.entity),
                entityId = requireNotNull(operationLogCommand.entityId),
                operation = requireNotNull(operationLogCommand.operation),
                comments = requireNotNull(operationLogCommand.comments),
                actionedBy = requireNotNull(user.toLong()),
            )

        operationLogRepository.save(operationLog)
    }
}
