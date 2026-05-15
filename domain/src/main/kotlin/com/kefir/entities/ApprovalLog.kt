package com.kefir.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "approval_log")
class ApprovalLog(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approval_log_id_seq_gen")
    @SequenceGenerator(name = "approval_log_id_seq_gen", sequenceName = "approval_log_id_seq", allocationSize = 1)
    val id: Long = 0,
    val entity: String,
    val approvableId: Long,
    val status: Long,
    val comments: String,
    val actionedBy: Long,
    val operationDate: OffsetDateTime = OffsetDateTime.now(),
)
