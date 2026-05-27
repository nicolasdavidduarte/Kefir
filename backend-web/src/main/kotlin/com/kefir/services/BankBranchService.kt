package com.kefir.services

import com.kefir.entities.BankBranch
import com.kefir.exceptions.BankBranchNotFoundException
import com.kefir.repositories.BankBranchRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BankBranchService(
    val bankBranchRepository: BankBranchRepository,
) {

    fun fetchByBranchNumberAndBank(branchNumber: Int, bankId: Int): BankBranch = bankBranchRepository.findByBranchNumberAndBankId(branchNumber, bankId).orElseThrow { throw BankBranchNotFoundException("Bank branch not found") }
}
