package com.kefir.repositories

import com.kefir.entities.LoanType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface LoanTypeRepository : JpaRepository<LoanType, Int> {
    fun findByNameIgnoringCase(name: String): Optional<LoanType>

    fun findAllByOrderByIdAsc(): List<LoanType>
}
