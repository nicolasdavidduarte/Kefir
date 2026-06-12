package com.kefir.repositories

import com.kefir.entities.LoanInstallment
import org.springframework.data.jpa.repository.JpaRepository

interface LoanInstallmentRepository : JpaRepository<LoanInstallment, Long> {
    fun findAllByOrderByNumberAsc(): List<LoanInstallment>
}
