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
@Table(name = "payment_method")
class PaymentMethod(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_seq_gen")
    @SequenceGenerator(name = "payment_method_seq_gen", sequenceName = "payment_method_seq", allocationSize = 1)
    val id: Int,
    val name: String,
    val status: Long,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false, updatable = true)
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
