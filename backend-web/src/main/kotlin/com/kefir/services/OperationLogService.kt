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

    fun log(
        operationLogCommand: OperationLogCommand,
    ) {
        val operationLog =
            OperationLog(
                operation = operationLogCommand.operation.name,
                entity = operationLogCommand.entity.name,
                entityId = requireNotNull(operationLogCommand.entityId),
                comments = requireNotNull(operationLogCommand.comments),
                user = auxAuthService.retrieveUserFromAuth(),
            )

        operationLogRepository.save(operationLog)
    }
}
