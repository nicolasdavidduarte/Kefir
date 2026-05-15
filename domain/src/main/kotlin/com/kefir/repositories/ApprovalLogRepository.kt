package com.kefir.repositories

import com.kefir.entities.ApprovalLog
import org.springframework.data.jpa.repository.JpaRepository

interface ApprovalLogRepository : JpaRepository<ApprovalLog, Long>
