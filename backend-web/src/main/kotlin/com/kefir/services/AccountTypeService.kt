package com.kefir.services

import com.kefir.entities.AccountType
import com.kefir.repositories.AccountTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountTypeService(
    val accountTypeRepository: AccountTypeRepository,
) {
    fun fetchByName(name: String?): AccountType = accountTypeRepository.findByNameIgnoreCase(name) ?: throw RuntimeException("Account type not found")
}
