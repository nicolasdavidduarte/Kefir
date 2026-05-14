package com.kefir.services

import com.kefir.entities.Account
import com.kefir.exceptions.AccountNotFoundException
import com.kefir.repositories.AccountRepository
import com.kefir.web.DTOs.AccountRequest
import com.kefir.web.DTOs.AccountResponse
import com.kefir.web.DTOs.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Transactional
@Service
class AccountService(val accountRepository: AccountRepository) {

    fun createAccount(accountRequest : AccountRequest) : AccountResponse {

        var CBU = generateCBUFirstBlock(requireNotNull(accountRequest.bank), requireNotNull(accountRequest.bankBranch))

        val savedAccount = accountRepository.save(Account(type = requireNotNull(accountRequest.type), customer = requireNotNull(accountRequest.customer) , currency = requireNotNull(accountRequest.currency), CBU = CBU, bank = requireNotNull(accountRequest.bank) , balance = requireNotNull(accountRequest.initialBalance)))

        savedAccount.CBU = generateCBUSecondBlock(savedAccount.CBU, savedAccount.id)

        return accountRepository.save(savedAccount).toResponse()
    }

    fun getAllAccounts(): List<AccountResponse> = accountRepository.findAll().map(Account::toResponse).toList().ifEmpty { throw AccountNotFoundException("No accounts found") }

    fun generateCBUFirstBlock(bank: Long, branch : Long) : String {
        // 1st block: Bank code (3) + Branch code (4) + Verification number (1)

        val CBU: String = bank.toString().padStart(3, '0') +
                branch.toString().padStart(4, '0') +
                (0..9).random().toString()

        return CBU
    }

    fun generateCBUSecondBlock(CBUFirstBlock: String, id: Long) : String {
        // 2nd block: Account number (13) + Verification number (1)

        val CBU = CBUFirstBlock +
                id.toString().padStart(13, '0') +
                (0..9).random().toString()

        return CBU
    }

}