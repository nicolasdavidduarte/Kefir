package com.kefir.services.account

import com.kefir.entities.Account
import com.kefir.entities.AccountType
import com.kefir.entities.BankBranch
import com.kefir.entities.Currency
import com.kefir.entities.Customer
import com.kefir.entities.User
import com.kefir.entities.close
import com.kefir.entities.open
import com.kefir.enums.EntityName
import com.kefir.enums.LogOperation
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.AccountRepository
import com.kefir.services.AccountTypeService
import com.kefir.services.BankBranchService
import com.kefir.services.CurrencyService
import com.kefir.services.CustomerService
import com.kefir.services.OperationLogService
import com.kefir.services.UserService
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
    private val accountRepository: AccountRepository,
    private val operationLogService: OperationLogService,
    private val authService: AuthService,
    private val userService: UserService,
    private val customerService: CustomerService,
    private val currencyService: CurrencyService,
    private val accountTypeService: AccountTypeService,
    private val bankBranchService: BankBranchService,
) {

    /**
     * Obtains all accounts from the database
     * @return list of accounts
     */
    @Cacheable("accounts")
    fun getAllAccounts(): List<AccountResponse> = accountRepository.findAllByOrderByIdAsc().sortedBy(Account::id).map(Account::toResponse).toList().ifEmpty {
        throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND)
    }

    fun getById(id: Long): AccountResponse {
        val account = accountRepository.findById(id).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        return account.toResponse()
    }

    /**
     * Creates a new account
     * @param [accountRequest] account request
     * @return account response
     */
    fun createAccount(accountRequest: AccountRequest): AccountResponse {
        val user: User = userService.getById(authService.currentUserId)

        val accountType: AccountType = accountTypeService.getByName(accountRequest.type?.dbName)

        val customer: Customer = customerService.getById(accountRequest.customerId)

        val bankBranch: BankBranch = bankBranchService.getByBranchNumberAndBank(requireNotNull(accountRequest.bankBranchId), requireNotNull(accountRequest.bankId))

        val currency: Currency = currencyService.getByIsoCode(requireNotNull(accountRequest.currencyIsoCode))

        val savedAccount =
            accountRepository.save(
                Account(
                    type = accountType,
                    customer = customer,
                    currency = currency,
                    bank = bankBranch.bank,
                    balance = accountRequest.initialBalance,
                    user = user,
                ),
            )

        savedAccount.cbu = CBUGenerator.generate(savedAccount.bank.id, requireNotNull(accountRequest.bankBranchId), savedAccount.id)

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
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.LOAN_NOT_FOUND) }

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

        return EntityOperationResponse(
            operation = "Opening",
            entity = "Account",
            id = id,
            message = "Account opened!",
            timestamp = LocalDateTime.now(),
        )
    }

    fun close(id: Long): EntityOperationResponse {
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

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

        return EntityOperationResponse(
            operation = "Closing",
            entity = "Account",
            id = id,
            message = "Account closed!",
            timestamp = LocalDateTime.now(),
        )
    }
}
