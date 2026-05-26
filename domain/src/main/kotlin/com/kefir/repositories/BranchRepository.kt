package com.kefir.repositories

import com.kefir.entities.BankBranch
import org.springframework.data.jpa.repository.JpaRepository

interface BranchRepository : JpaRepository<BankBranch, Int>
