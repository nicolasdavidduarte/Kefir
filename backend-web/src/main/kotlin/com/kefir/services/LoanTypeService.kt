package com.kefir.services

import com.kefir.entities.LoanType
import com.kefir.repositories.LoanTypeRepository
import com.kefir.web.DTOs.LoanTypeRequest
import com.kefir.web.DTOs.LoanTypeResponse
import com.kefir.web.DTOs.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LoanTypeService(private val loanTypeRepository: LoanTypeRepository) {

    fun getAllLoanTypes(): List<LoanType> = loanTypeRepository.findAll()

    @Transactional
    fun create(loanTypeRequest : LoanTypeRequest) : LoanTypeResponse {

        val loanType = LoanType(name = loanTypeRequest.name, description = loanTypeRequest.description)

        val savedLoanType = loanTypeRepository.save(loanType)

        return savedLoanType.toResponse()

    }

}