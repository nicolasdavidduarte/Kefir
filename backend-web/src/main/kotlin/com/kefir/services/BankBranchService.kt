package com.kefir.services

import com.kefir.entities.BankBranch
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.BankBranchRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BankBranchService(
    private val bankBranchRepository: BankBranchRepository,
) {

    @Transactional(readOnly = true)
    fun getByBranchNumberAndBank(branchNumber: Int, bankId: Int): BankBranch = bankBranchRepository.findByBranchNumberAndBankId(branchNumber, bankId).orElseThrow { throw ApiException(ErrorCode.BANK_BRANCH_NOT_FOUND) }
}
