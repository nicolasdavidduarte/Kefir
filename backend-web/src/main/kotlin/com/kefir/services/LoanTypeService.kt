package com.kefir.services

import com.kefir.entities.LoanType
import com.kefir.enums.LoanTypeName
import com.kefir.exceptions.LoanTypeNotFoundException
import com.kefir.repositories.LoanTypeRepository
import com.kefir.web.dtos.LoanTypeRequest
import com.kefir.web.dtos.LoanTypeResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LoanTypeService(
    private val loanTypeRepository: LoanTypeRepository,
    private val auxAuthService: AuxAuthService,
) {
    fun getAllLoanTypes(): List<LoanType> = loanTypeRepository.findAll()

    fun getByNameIgnoringCase(name: LoanTypeName): LoanType = loanTypeRepository.findByNameIgnoringCase(name.name).orElseThrow { throw LoanTypeNotFoundException() }

    @Transactional
    fun create(loanTypeRequest: LoanTypeRequest): LoanTypeResponse {
        val loanType = LoanType(
            name = loanTypeRequest.name,
            description = loanTypeRequest.description,
            user = auxAuthService.getUserFromAuth(),
        )

        val savedLoanType = loanTypeRepository.save(loanType)

        return savedLoanType.toResponse()
    }
}
