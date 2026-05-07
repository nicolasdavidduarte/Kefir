package com.kefir.services

import com.kefir.entities.LoanType
import com.kefir.repositories.LoanTypeRepository
import org.springframework.stereotype.Service

@Service
class LoanTypeService(private val loanTypeRepository: LoanTypeRepository) {

    fun getAllLoanTypes(): List<LoanType> = loanTypeRepository.findAll()

    fun create(loanType : LoanType) : LoanType = loanTypeRepository.save(loanType)

}