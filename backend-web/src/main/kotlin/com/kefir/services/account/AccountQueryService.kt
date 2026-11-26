package com.kefir.services.account

import com.kefir.entities.Account
import com.kefir.enums.AccountStatus
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.persistence.repositories.AccountRepository
import com.kefir.web.dtos.account.AccountResponse
import com.kefir.web.dtos.account.toResponse
import com.kefir.web.utils.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountQueryService(
    private val accountRepository: AccountRepository,
) {

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
    fun getAllWithResponse(pageable: Pageable): PageResponse<AccountResponse> {
        val page = accountRepository.findAllByOrderByIdAsc(pageable)
        return PageResponse.from(page, Account::toResponse)
    }

    @Transactional(readOnly = true)
    fun getAllByCustomerWithResponse(customerId: Long, pageable: Pageable): PageResponse<AccountResponse> {
        val page = accountRepository.findAllByCustomerIdOrderByIdAsc(customerId, pageable)
        return PageResponse.from(page, Account::toResponse)
    }

    @Transactional(readOnly = true)
    fun getAllByCustomerByStatus(customerId: Long, status: AccountStatus): List<Account> = accountRepository.findAllByCustomerIdAndStatus(customerId, status)
}
