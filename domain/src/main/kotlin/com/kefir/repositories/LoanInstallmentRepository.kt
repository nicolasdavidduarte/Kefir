package com.kefir.repositories

import com.kefir.entities.LoanInstallment
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LoanInstallmentRepository : JpaRepository<LoanInstallment, Long> {

    @EntityGraph(attributePaths = ["loan", "createdBy", "updatedBy"])
    fun findAllByLoanIdOrderByNumberAsc(loan: Long): List<LoanInstallment>

    @Query(
        """
        SELECT li 
        FROM LoanInstallment li
        WHERE li.loan.id = :loanId AND li.status IN ('PAYMENT_PENDING', 'OVERDUE')
    """,
    )
    fun findByLoanIdForChargeOff(@Param("loanId") loanId: Long): List<LoanInstallment>
}
