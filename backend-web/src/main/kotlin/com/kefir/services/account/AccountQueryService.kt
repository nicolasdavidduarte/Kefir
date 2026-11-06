package com.kefir.services.account

import com.kefir.entities.Account
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.AccountRepository
import com.kefir.web.dtos.account.AccountResponse
import com.kefir.web.dtos.account.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountQueryService(
    private val accountRepository: AccountRepository,
) {

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): List<AccountResponse> = accountRepository.findAllByOrderByIdAsc(pageable).map(Account::toResponse).toList()

    @Transactional(readOnly = true)
    fun getById(id: Long): Account = accountRepository.findById(id).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

    @Transactional(readOnly = true)
    fun getByIdAndCustomerId(id: Long, customerId: Long): Account = accountRepository.findByIdAndCustomerId(id, customerId).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

    @Transactional(readOnly = true)
    fun getByIdWithResponse(id: Long): AccountResponse {
        val account = accountRepository.findByIdWithDetails(id).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        return account.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllByCustomerWithResponse(customerId: Long): List<AccountResponse> = accountRepository.findAllByCustomerId(customerId).map(Account::toResponse)

    @Transactional
    fun getByLoanIdForUpdate(loanId: Long): Account = accountRepository.findByLoanIdForUpdate(loanId).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }
}
