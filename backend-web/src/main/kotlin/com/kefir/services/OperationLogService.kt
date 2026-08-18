package com.kefir.services

import com.kefir.entities.OperationLog
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.OperationLogRepository
import com.kefir.web.dtos.operationLog.OperationLogCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationLogService(
    private val operationLogRepository: OperationLogRepository,
    private val userService: UserService,
    private val authService: AuthService,
) {

    @Transactional
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
