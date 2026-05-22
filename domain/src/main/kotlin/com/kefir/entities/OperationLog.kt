package com.kefir.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "operation_log")
class OperationLog(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_log_id_seq_gen")
    @SequenceGenerator(name = "operation_log_id_seq_gen", sequenceName = "operation_log_id_seq", allocationSize = 1)
    val id: Long = 0,
    val entity: String,
    val entityId: Long,
    val operation: String,
    val comments: String,
    val actionedBy: Long,
    val operationDate: OffsetDateTime = OffsetDateTime.now(),
)
