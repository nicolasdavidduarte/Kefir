package com.kefir.services

import com.kefir.entities.Account
import com.kefir.entities.CoreUser
import com.kefir.entities.close
import com.kefir.entities.open
import com.kefir.enums.EntityName
import com.kefir.enums.LogOperation
import com.kefir.exceptions.AccountNotFoundException
import com.kefir.repositories.AccountRepository
import com.kefir.services.aux.account.CBUGenerator
import com.kefir.web.dtos.AccountRequest
import com.kefir.web.dtos.AccountResponse
import com.kefir.web.dtos.EntityOperationResponse
import com.kefir.web.dtos.OperationLogCommand
import com.kefir.web.dtos.toResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Transactional
@Service
class AccountService(
    val accountRepository: AccountRepository,
    val operationLogService: OperationLogService,
    val auxAuthService: AuxAuthService,
) {

    /**
     * Obtains all accounts from the database
     * @return list of accounts
     */
    @Cacheable("accounts")
    fun getAllAccounts(): List<AccountResponse> = accountRepository.findAll().map(Account::toResponse).toList().ifEmpty {
        throw AccountNotFoundException("No accounts found")
    }

    /**
     * Creates a new account
     * @param [accountRequest] account request
     * @return account response
     */
    fun createAccount(accountRequest: AccountRequest): AccountResponse {
        val user: CoreUser = auxAuthService.retrieveUserFromAuth()

        val savedAccount =
            accountRepository.save(
                Account(
                    type = requireNotNull(accountRequest.type),
                    customer = requireNotNull(accountRequest.customer),
                    currency = requireNotNull(accountRequest.currency),
                    bank = requireNotNull(accountRequest.bank),
                    balance = accountRequest.initialBalance,
                    user = user,
                ),
            )

        savedAccount.cbu = CBUGenerator.generate(savedAccount.bank.id, requireNotNull(accountRequest.bankBranch!!.id), savedAccount.id)

        operationLogService.log(
            OperationLogCommand(
                operation = LogOperation.CREATION,
                entity = EntityName.ACCOUNT,
                entityId = savedAccount.id,
                comments = "Account with id: ${savedAccount.id} created",
            ),
        )

        return accountRepository.save(savedAccount).toResponse()
    }

    fun open(id: Long): EntityOperationResponse {
        val account = accountRepository.findById(id).orElseThrow { throw AccountNotFoundException("Account not found") }

        account.open()
        account.updatedAt = OffsetDateTime.now()

        accountRepository.save(account)

        operationLogService.log(
            OperationLogCommand(
                operation = LogOperation.OPENING,
                entity = EntityName.ACCOUNT,
                entityId = account.id,
                comments = "Account with id: ${account.id} opened",
            ),
        )

        return EntityOperationResponse(operation = "Opening", entity = "Account", id = id, message = "Account opened!", timestamp = LocalDateTime.now())
    }

    fun close(id: Long): EntityOperationResponse {
        val account = accountRepository.findById(id).orElseThrow { throw AccountNotFoundException("Account not found") }

        account.close()
        account.updatedAt = OffsetDateTime.now()

        accountRepository.save(account)

        operationLogService.log(
            OperationLogCommand(
                operation = LogOperation.CLOSING,
                entity = EntityName.ACCOUNT,
                entityId = account.id,
                comments = "Account with id: ${account.id} closed",
            ),
        )

        return EntityOperationResponse(operation = "Closing", entity = "Account", id = id, message = "Account closed!", timestamp = LocalDateTime.now())
    }
}
