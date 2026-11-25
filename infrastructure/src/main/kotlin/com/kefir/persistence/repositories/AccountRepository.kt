package com.kefir.persistence.repositories

import com.kefir.entities.Account
import com.kefir.enums.AccountStatus
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AccountRepository : JpaRepository<Account, Long> {

    fun findByIdAndCustomerId(id: Long, customerId: Long): Optional<Account>

    @EntityGraph(attributePaths = ["type", "customer", "currency", "bank", "createdBy", "updatedBy"])
    @Query(value = "select a from Account a where a.id = :id")
    fun findByIdWithDetails(@Param("id") id: Long): Optional<Account>

    @EntityGraph(attributePaths = ["type", "customer", "currency", "bank", "createdBy", "updatedBy"])
    fun findAllByOrderByIdAsc(pageable: Pageable): Page<Account>

    @EntityGraph(attributePaths = ["type", "customer", "currency", "bank", "createdBy", "updatedBy"])
    fun findAllByCustomerIdOrderByIdAsc(customerId: Long, pageable: Pageable): Page<Account>

    @EntityGraph(attributePaths = ["type", "customer", "currency", "bank", "createdBy", "updatedBy"])
    fun findAllByCustomerIdAndStatus(customerId: Long, status: AccountStatus): List<Account>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select a from Account a where a.id = :id")
    fun findByIdForUpdate(id: Long): Optional<Account>

    @Query(value = "select nextval('account_number_seq')", nativeQuery = true)
    fun findNextAccountNumberSequence(): Long

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select a from Account a join Loan l on l.account.id = a.id where l.id = :loanId")
    fun findByLoanIdForUpdate(loanId: Long): Optional<Account>
}
