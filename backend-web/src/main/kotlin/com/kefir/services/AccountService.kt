package com.kefir.services

import com.kefir.entities.Account
import com.kefir.entities.close
import com.kefir.entities.open
import com.kefir.exceptions.AccountNotFoundException
import com.kefir.repositories.AccountRepository
import com.kefir.services.aux.account.CBUGenerator
import com.kefir.web.dtos.AccountRequest
import com.kefir.web.dtos.AccountResponse
import com.kefir.web.dtos.EntityOperationResponse
import com.kefir.web.dtos.OperationLogCommand
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.Long

@Transactional
@Service
class AccountService(
    val accountRepository: AccountRepository,
    val operationLogService: OperationLogService,
) {

    /**
     * Obtains all accounts from the database
     * @return list of accounts
     */
    fun getAllAccounts(): List<AccountResponse> = accountRepository.findAll().map(Account::toResponse).toList().ifEmpty {
        throw AccountNotFoundException("No accounts found")
    }

    /**
     * Creates a new account
     * @param [accountRequest] account request
     * @return account response
     */
    fun createAccount(accountRequest: AccountRequest): AccountResponse {
        val savedAccount =
            accountRepository.save(
                Account(
                    type = requireNotNull(accountRequest.type),
                    customer = requireNotNull(accountRequest.customer),
                    currency = requireNotNull(accountRequest.currency),
                    bank = requireNotNull(accountRequest.bank),
                    balance = accountRequest.initialBalance,
                ),
            )

        savedAccount.cbu = CBUGenerator.generate(savedAccount.bank, requireNotNull(accountRequest.bankBranch), savedAccount.id)

        return accountRepository.save(savedAccount).toResponse()
    }

    fun open(id: Long): EntityOperationResponse {
        val account = accountRepository.findById(id).orElseThrow { throw AccountNotFoundException("Account not found") }

        account.open()

        accountRepository.save(account)

        createOperationLogFor("Opening", id, "Closing of account")

        return EntityOperationResponse(operation = "Opening", entity = "Account", id = id, message = "Account opened!", timestamp = LocalDateTime.now())
    }

    fun close(id: Long): EntityOperationResponse {
        val account = accountRepository.findById(id).orElseThrow { throw AccountNotFoundException("Account not found") }

        account.close()

        accountRepository.save(account)

        createOperationLogFor("Closing", id, "Opening of account")

        return EntityOperationResponse(operation = "Closing", entity = "Account", id = id, message = "Account closed!", timestamp = LocalDateTime.now())
    }

    fun createOperationLogFor(operation: String, id: Long, comments: String) {
        val operationLogCommand =
            OperationLogCommand(
                operation = operation,
                entity = "Account",
                entityId = id,
                comments = comments,
            )

        operationLogService.create(operationLogCommand)
    }
}
