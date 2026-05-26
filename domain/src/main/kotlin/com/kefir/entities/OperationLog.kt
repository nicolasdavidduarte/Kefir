package com.kefir.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
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

    @Column(name = "entity", nullable = false)
    val entity: String,

    @Column(name = "entity_id", nullable = false)
    val entityId: Long,

    @Column(name = "operation", nullable = false)
    val operation: String,

    @Column(name = "comments", nullable = false)
    val comments: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: CoreUser,

    @Column(name = "operation_date", nullable = false)
    val operationDate: OffsetDateTime = OffsetDateTime.now(),
)
