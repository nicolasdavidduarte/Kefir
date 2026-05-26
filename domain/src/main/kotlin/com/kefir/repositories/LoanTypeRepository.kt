package com.kefir.repositories

import com.kefir.entities.LoanType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoanTypeRepository : JpaRepository<LoanType, Int>
