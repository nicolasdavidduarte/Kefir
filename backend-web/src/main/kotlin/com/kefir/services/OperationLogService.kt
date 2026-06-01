package com.kefir.services

import com.kefir.entities.OperationLog
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.OperationLogRepository
import com.kefir.web.dtos.OperationLogCommand
import org.springframework.stereotype.Service

@Service
class OperationLogService(
    val operationLogRepository: OperationLogRepository,
    val userService: UserService,
    val authService: AuthService,
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
                user = userService.getById(authService.currentUserId),
            )

        operationLogRepository.save(operationLog)
    }
}
