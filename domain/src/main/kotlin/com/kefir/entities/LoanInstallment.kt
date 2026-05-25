package com.kefir.entities

import com.kefir.enums.LoanInstallmentStatus
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
    val id: Long = 0,
    val loan: Long,
    val number: Int,
    val amount: BigDecimal,
    val paymentDueDate: OffsetDateTime,
    var status: Long,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false, updatable = true)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        @JvmStatic
        fun createNew(
            loan: Long,
            number: Int,
            amount: BigDecimal,
        ): LoanInstallment {
            val now = OffsetDateTime.now()
            return LoanInstallment(
                loan = loan,
                number = number,
                amount = amount,
                paymentDueDate = now.plusMonths(1),
                status = LoanInstallmentStatus.PAYMENT_PENDING.id,
            )
        }
    }
}
