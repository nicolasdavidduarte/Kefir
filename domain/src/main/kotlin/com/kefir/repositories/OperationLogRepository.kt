package com.kefir.repositories

import com.kefir.entities.OperationLog
import org.springframework.data.jpa.repository.JpaRepository

interface OperationLogRepository : JpaRepository<OperationLog, Long>
