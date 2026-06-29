package com.kefir.services

import com.kefir.entities.AccountType
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.AccountTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountTypeService(
    private val accountTypeRepository: AccountTypeRepository,
) {
    @Transactional(readOnly = true)
    fun getByName(name: String?): AccountType = accountTypeRepository.findByNameIgnoreCase(name).orElseThrow { ApiException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND) }
}
