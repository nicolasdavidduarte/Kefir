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
@Table(name = "payment_method")
class PaymentMethod(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_seq_gen")
    @SequenceGenerator(name = "payment_method_seq_gen", sequenceName = "payment_method_seq", allocationSize = 1)
    val id: Int,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "enabled", nullable = false, updatable = true)
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
) {
    override fun toString(): String = "{Id: $id / Name: $name / Enabled: $enabled}"
}
