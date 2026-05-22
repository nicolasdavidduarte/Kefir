package com.kefir.entities

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
@Table(name = "loan_installment")
class LoanInstallment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_installment_id_seq_gen")
    @SequenceGenerator(name = "loan_installment_id_seq_gen", sequenceName = "loan_installment_id_seq", allocationSize = 1)
    val id: Long,
    val loan: Long,
    val number: Int,
    val amount: BigDecimal,
    val paymentDueDate: OffsetDateTime,
    val status: Long,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false, updatable = true)
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
