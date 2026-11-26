package com.kefir.services.account

import com.kefir.entities.Account
import com.kefir.entities.User
import com.kefir.enums.CustomerStatus
import com.kefir.enums.EntityName
import com.kefir.enums.LogOperation
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
import com.kefir.persistence.repositories.AccountRepository
import com.kefir.persistence.repositories.CustomerRepository
import com.kefir.persistence.repositories.LoanRepository
import com.kefir.services.accounttype.AccountTypeService
import com.kefir.services.bankbranch.BankBranchService
import com.kefir.services.currency.CurrencyService
import com.kefir.services.operationlog.OperationLogService
import com.kefir.services.user.UserService
import com.kefir.web.dtos.account.AccountRequest
import com.kefir.web.dtos.account.AccountResponse
import com.kefir.web.dtos.account.toResponse
import com.kefir.web.dtos.operationLog.OperationLogCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountCommandService(
    private val accountRepository: AccountRepository,
    private val loanRepository: LoanRepository,
    private val operationLogService: OperationLogService,
    private val authService: AuthService,
    private val userService: UserService,
    private val customerRepository: CustomerRepository,
    private val currencyService: CurrencyService,
    private val accountTypeService: AccountTypeService,
    private val bankBranchService: BankBranchService,
) {

    @Transactional
    fun create(accountRequest: AccountRequest): AccountResponse {
        val customer = customerRepository.findById(requireNotNull(accountRequest.customerId)).orElseThrow { ApiException(ErrorCode.CUSTOMER_NOT_FOUND) }
        if (!customer.isActive) throw ApiException(ErrorCode.CUSTOMER_NOT_VALID)

        val user = userService.getById(authService.currentUserId)
        val accountType = accountTypeService.getByName(accountRequest.type?.dbName)
        val bankBranch = bankBranchService.getByBranchNumberAndBank(requireNotNull(accountRequest.bankBranchId), requireNotNull(accountRequest.bankId))
        val currency = currencyService.getByIsoCode(requireNotNull(accountRequest.currencyIsoCode))
        val sequence = accountRepository.findNextAccountNumberSequence()
        val accountNumber = AccountNumberGenerator.generate(accountTypeCode = accountType.code, sequence = sequence)

        val cbu = CBUGenerator.generate(bankBranch.bank.id, requireNotNull(accountRequest.bankBranchId), accountNumber)

        val account = Account.create(accountType, customer, currency, bankBranch, accountRequest.initialBalance, user, accountNumber, cbu)

        accountRepository.save(account)

        operationLogService.log(
            OperationLogCommand(
                operation = LogOperation.CREATION,
                entity = EntityName.ACCOUNT,
                entityId = account.id,
                comments = "Account with id: ${account.id} created",
                user = user,
            ),
        )

        return account.toResponse()
    }

    @Transactional
    fun open(id: Long): AccountResponse {
        val account = accountRepository.findByIdForUpdate(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        if (account.customer.status != CustomerStatus.ACTIVE) {
            throw ApiException(ErrorCode.CUSTOMER_NOT_VALID)
        }

        val user = userService.getById(authService.currentUserId)

        account.open(user)

        operationLogService.log(
            OperationLogCommand(
                operation = LogOperation.OPENING,
                entity = EntityName.ACCOUNT,
                entityId = account.id,
                comments = "Account with id: ${account.id} opened",
                user = user,
            ),
        )

        return account.toResponse()
    }

    @Transactional
    fun suspend(id: Long, reason: String): AccountResponse {
        val account = accountRepository.findByIdForUpdate(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        val user: User = userService.getById(authService.currentUserId)

        account.suspend(user)

        operationLogService.log(
            OperationLogCommand(
                LogOperation.SUSPENSION,
                EntityName.ACCOUNT,
                id,
                reason,
                user,
            ),
        )

        return account.toResponse()
    }

    @Transactional
    fun close(id: Long): AccountResponse {
        val account = accountRepository.findByIdForUpdate(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        val user = userService.getById(authService.currentUserId)

        val loans = loanRepository.findAllByAccountId(account.id)

        account.close(user, loans)

        operationLogService.log(
            OperationLogCommand(
                LogOperation.CLOSING,
                EntityName.ACCOUNT,
                id,
                "Account with id: ${account.id} closed",
                user,
            ),
        )

        return account.toResponse()
    }

    @Transactional
    fun getByLoanIdForUpdate(loanId: Long): Account = accountRepository.findByLoanIdForUpdate(loanId).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }
}
