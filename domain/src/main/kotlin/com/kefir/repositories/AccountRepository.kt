package com.kefir.repositories

import com.kefir.entities.Account
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AccountRepository : JpaRepository<Account, Long> {

    fun findAllByOrderByIdAsc(): List<Account>

    fun findAllByCustomerId(customerId: Long): List<Account>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select a from Account a where a.id = :id")
    fun findByIdForUpdate(id: Long): Optional<Account>

    @Query(value = "SELECT nextval('account_number_seq')", nativeQuery = true)
    fun findNextAccountNumberSequence(): Long
}
