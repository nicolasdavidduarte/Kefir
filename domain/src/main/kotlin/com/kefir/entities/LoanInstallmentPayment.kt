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
@Table(name = "loan_installment_payment")
class LoanInstallmentPayment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_installment_payment_id_seq_gen")
    @SequenceGenerator(name = "loan_installment_payment_id_seq_gen", sequenceName = "loan_installment_payment_id_seq", allocationSize = 1)
    val id: Long,
    val loanInstallment: Long,
    val amountPaid: BigDecimal,
    val paymentDate: OffsetDateTime,
    val paymentMethod: Int,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
)
