package com.kefir.repositories

import com.kefir.entities.BankBranch
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface BankBranchRepository : JpaRepository<BankBranch, Int> {
    fun findByBranchNumberAndBankId(branchNumber: Int, bankId: Int): Optional<BankBranch>
}
