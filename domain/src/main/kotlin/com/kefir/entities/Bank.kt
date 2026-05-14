package com.kefir.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "bank")
class Bank (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_id_seq_gen")
    @SequenceGenerator(name = "bank_id_seq_gen", sequenceName = "bank_id_seq", allocationSize = 1)
    val id: Long = 0,
    val name: String,
    val status: Int,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false, updatable = true)
    val updatedAt: OffsetDateTime = OffsetDateTime.now()
)