package com.kefir.entities

import com.kefir.enums.BankStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(name = "bank")
class Bank(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_id_seq_gen")
    @SequenceGenerator(name = "bank_id_seq_gen", sequenceName = "bank_id_seq", allocationSize = 1)
    val id: Int = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: BankStatus = BankStatus.APPROVAL_PENDING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: CoreUser,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false, updatable = true)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)

fun Bank.enable() {
    status = BankStatus.ACTIVE
}

fun Bank.disable() {
    status = BankStatus.CLOSED
}
