package com.kefir.web.dtos

import com.kefir.entities.LoanInstallment
import java.math.BigDecimal
import java.time.OffsetDateTime

data class LoanInstallmentResponse(
    val loanId: Long,
    val number: Int,
    val principalAmount: BigDecimal,
    val interestAmount: BigDecimal,
    val totalAmount: BigDecimal,
    val paymentDueDate: OffsetDateTime,
    val remainingBalance: BigDecimal,
    val status: String,
    val createdBy: String,
    val createdAt: OffsetDateTime,
    val updatedBy: String,
    val updatedAt: OffsetDateTime,
)
fun LoanInstallment.toResponse() = LoanInstallmentResponse(
    loanId = this.loan.id,
    number = this.number,
    principalAmount = this.principalAmount,
    interestAmount = this.interestAmount,
    totalAmount = this.totalAmount,
    paymentDueDate = this.paymentDueDate,
    remainingBalance = this.remainingBalance,
    status = this.status.name,
    createdBy = this.createdBy.username,
    createdAt = this.createdAt,
    updatedBy = this.updatedBy.username,
    updatedAt = this.updatedAt,
)
