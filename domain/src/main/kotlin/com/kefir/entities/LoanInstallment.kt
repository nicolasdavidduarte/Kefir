package com.kefir.entities

import com.kefir.enums.LoanInstallmentStatus
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
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "loan_installment")
class LoanInstallment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_installment_id_seq_gen")
    @SequenceGenerator(name = "loan_installment_id_seq_gen", sequenceName = "loan_installment_id_seq", allocationSize = 1)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    val loan: Loan,

    @Column(name = "number", nullable = false)
    val number: Int,

    @Column(name = "principal_amount", nullable = false)
    val principalAmount: BigDecimal,

    @Column(name = "interest_amount", nullable = false)
    val interestAmount: BigDecimal,

    @Column(name = "total_amount", nullable = false)
    val totalAmount: BigDecimal,

    @Column(name = "payment_due_date", nullable = false)
    val paymentDueDate: OffsetDateTime,

    @Column(name = "remaining_balance", nullable = false)
    val remainingBalance: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: LoanInstallmentStatus = LoanInstallmentStatus.PAYMENT_PENDING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false, updatable = true)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        @JvmStatic
        fun createNew(
            loan: Loan,
            number: Int,
            principalAmount: BigDecimal,
            interestAmount: BigDecimal,
            totalAmount: BigDecimal,
            remainingBalance: BigDecimal,
            paymentDueDate: OffsetDateTime,
            user: User,
        ): LoanInstallment = LoanInstallment(
            loan = loan,
            number = number,
            principalAmount = principalAmount,
            interestAmount = interestAmount,
            totalAmount = totalAmount,
            remainingBalance = remainingBalance,
            paymentDueDate = paymentDueDate,
            status = LoanInstallmentStatus.PAYMENT_PENDING,
            user = user,
        )
    }
}
