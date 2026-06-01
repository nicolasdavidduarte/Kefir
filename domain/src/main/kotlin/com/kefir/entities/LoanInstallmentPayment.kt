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
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "loan_installment_payment")
class LoanInstallmentPayment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_installment_payment_id_seq_gen")
    @SequenceGenerator(name = "loan_installment_payment_id_seq_gen", sequenceName = "loan_installment_payment_id_seq", allocationSize = 1)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_installment_id", nullable = false)
    val loanInstallment: LoanInstallment,

    @Column(name = "amount_paid", nullable = false)
    val amountPaid: BigDecimal,

    @Column(name = "payment_date", nullable = false)
    val paymentDate: OffsetDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method", nullable = false)
    val paymentMethod: PaymentMethod,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
)
