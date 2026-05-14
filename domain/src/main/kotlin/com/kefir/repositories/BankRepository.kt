package com.kefir.repositories

import com.kefir.entities.Bank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BankRepository : JpaRepository<Bank, Long>
