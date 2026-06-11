package com.kefir.services

import com.kefir.entities.LoanType
import com.kefir.enums.LoanTypeName
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
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
    private val userService: UserService,
    private val authService: AuthService,
) {
    fun getAllLoanTypesWithResponse(): List<LoanTypeResponse> = loanTypeRepository.findAllByOrderByIdAsc().map(
        LoanType::toResponse)

    fun getByNameIgnoringCase(name: LoanTypeName): LoanType = loanTypeRepository.findByNameIgnoringCase(name.name).orElseThrow { throw ApiException(ErrorCode.LOAN_TYPE_NOT_FOUND) }

    @Transactional
    fun create(loanTypeRequest: LoanTypeRequest): LoanTypeResponse {
        val loanType = LoanType(
            name = loanTypeRequest.name,
            description = loanTypeRequest.description,
            annualInterestRate = loanTypeRequest.baseInterestRate,
            user = userService.getById(authService.currentUserId),
        )

        val savedLoanType = loanTypeRepository.save(loanType)

        return savedLoanType.toResponse()
    }
}
