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
@Table(name = "document_type")
class DocumentType(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_type_id_seq_gen")
    @SequenceGenerator(name = "document_type_id_seq_gen", sequenceName = "document_type_id_seq", allocationSize = 1)
    val id: Int = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: User,

    @Column(name = "updated_at", nullable = false, updatable = true)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
