package com.kefir.services

import com.kefir.entities.ApprovalLog
import com.kefir.repositories.ApprovalLogRepository
import com.kefir.web.dtos.ApprovalLogRequest
import org.springframework.stereotype.Service

@Service
class ApprovalLogService(
    val approvalLogRepository: ApprovalLogRepository,
) {
    fun create(approvalRequest: ApprovalLogRequest) {
        val approvalLog =
            ApprovalLog(
                entity = requireNotNull(approvalRequest.entity),
                approvableId = requireNotNull(approvalRequest.approvableId),
                status = requireNotNull(approvalRequest.status),
                comments = requireNotNull(approvalRequest.comments),
                actionedBy = requireNotNull(approvalRequest.actionedBy),
            )

        approvalLogRepository.save(approvalLog)
    }
}
