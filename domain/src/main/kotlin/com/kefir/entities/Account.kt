package com.kefir.entities

import com.kefir.enums.AccountStatus
import com.kefir.exceptions.DomainErrorCode
import com.kefir.exceptions.DomainException
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
import kotlin.minus
import kotlin.plus

@Entity
@Table(name = "account")
class Account(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq_gen")
    @SequenceGenerator(name = "account_id_seq_gen", sequenceName = "account_id_seq", allocationSize = 1)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    val type: AccountType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    val currency: Currency,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    val bank: Bank,

    @Column(name = "account_number", nullable = false)
    val accountNumber: String,

    @Column(name = "cbu", nullable = false)
    var cbu: String = "",

    @Column(name = "balance", nullable = false, updatable = true)
    var balance: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: AccountStatus = AccountStatus.PENDING,

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
    override fun toString(): String = "{Id: $id / CBU: $cbu / Balance: $balance / Status: $status}"

    fun validateCanBeUsedToCreateLoan() {
        if (status != AccountStatus.OPENED) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID)
        }
    }

    fun addBalance(amount: BigDecimal) {
        if (this.status != AccountStatus.OPENED) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID)
        }

        if (amount.signum() < 0) {
            throw DomainException(DomainErrorCode.INVALID_AMOUNT)
        }

        this.balance += amount
    }

    fun subtractBalance(amount: BigDecimal) {
        if (this.status != AccountStatus.OPENED) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID)
        }

        if (amount.signum() < 0) {
            throw DomainException(DomainErrorCode.INVALID_AMOUNT)
        }

        if (this.balance < amount) {
            throw DomainException(DomainErrorCode.INSUFFICIENT_FUNDS)
        }

        this.balance -= amount
    }

    fun open(updatedBy: User) {
        if (this.status != AccountStatus.PENDING) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID)
        }

        this.status = AccountStatus.OPENED
        this.updatedAt = OffsetDateTime.now()
        this.updatedBy = updatedBy
    }

    fun suspend(updatedBy: User) {
        if (this.status != AccountStatus.OPENED) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID)
        }

        this.status = AccountStatus.SUSPENDED
        this.updatedAt = OffsetDateTime.now()
        this.updatedBy = updatedBy
    }

    fun close(updatedBy: User, loans: List<Loan>) {
        if ((this.status != AccountStatus.OPENED) && (this.status != AccountStatus.PENDING)) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID)
        }

        val loanPending = loans.any { it.isActive }

        if (loanPending) {
            throw DomainException(DomainErrorCode.ACCOUNT_NOT_VALID_FOR_CLOSURE)
        }

        this.status = AccountStatus.CLOSED
        this.updatedAt = OffsetDateTime.now()
        this.updatedBy = updatedBy
    }

    companion object {
        fun create(
            accountType: AccountType,
            customer: Customer,
            currency: Currency,
            bankBranch: BankBranch,
            initialBalance: BigDecimal,
            createdBy: User,
            accountNumber: String,
            cbu: String,
        ): Account = Account(
            type = accountType,
            customer = customer,
            currency = currency,
            bank = bankBranch.bank,
            balance = initialBalance,
            createdBy = createdBy,
            updatedBy = createdBy,
            accountNumber = accountNumber,
            cbu = cbu,
        )
    }
}
