package com.kefir.entities

import com.kefir.enums.AccountStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "account")
class Account(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq_gen")
    @SequenceGenerator(name = "account_id_seq_gen", sequenceName = "account_id_seq", allocationSize = 1)
    val id: Long = 0,
    val type: Long,
    val customer: Long,
    val currency: Long,
    val bank: Long,
    var cbu: String = "",
    val balance: BigDecimal,
    var status: Long = 0,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false, updatable = true)
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)

fun Account.open() {
    status = AccountStatus.OPENED.id
}

fun Account.close() {
    status = AccountStatus.CLOSED.id
}
