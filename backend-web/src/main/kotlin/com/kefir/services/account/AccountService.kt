package com.kefir.services.account

import com.kefir.entities.Account
import com.kefir.entities.User
import com.kefir.enums.AccountStatus
import com.kefir.enums.CustomerStatus
import com.kefir.enums.EntityName
import com.kefir.enums.LoanStatus
import com.kefir.enums.LogOperation
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
import com.kefir.repositories.AccountRepository
import com.kefir.repositories.LoanRepository
import com.kefir.services.AccountTypeService
import com.kefir.services.BankBranchService
import com.kefir.services.CurrencyService
import com.kefir.services.CustomerService
import com.kefir.services.OperationLogService
import com.kefir.services.UserService
import com.kefir.web.dtos.account.AccountRequest
import com.kefir.web.dtos.account.AccountResponse
import com.kefir.web.dtos.account.toResponse
import com.kefir.web.dtos.operationLog.OperationLogCommand
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val loanRepository: LoanRepository,
    private val operationLogService: OperationLogService,
    private val authService: AuthService,
    private val userService: UserService,
    private val customerService: CustomerService,
    private val currencyService: CurrencyService,
    private val accountTypeService: AccountTypeService,
    private val bankBranchService: BankBranchService,
) {

    companion object {
        private const val SYSTEM_USER = 1
    }

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
    fun addBalance(accountId: Long, amount: BigDecimal) {
        val account = accountRepository.findByIdForUpdate(accountId).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        if (account.status != AccountStatus.OPENED) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID)
        }

        account.balance += amount
    }

    @Transactional
    fun subtractBalance(accountId: Long, amount: BigDecimal) {
        val account = accountRepository.findByIdForUpdate(accountId).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        if (account.status != AccountStatus.OPENED) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID)
        }

        if (account.balance < amount) {
            throw ApiException(ErrorCode.ACCOUNT_WITHOUT_FUNDS)
        }

        account.balance -= amount
    }

    @Transactional
    fun create(accountRequest: AccountRequest): AccountResponse {
        val customer = customerService.getById(accountRequest.customerId)

        if (customer.status != CustomerStatus.ACTIVE) {
            throw ApiException(ErrorCode.CUSTOMER_NOT_VALID)
        }

        val user = userService.getById(authService.currentUserId)

        val accountType = accountTypeService.getByName(accountRequest.type?.dbName)

        val bankBranch = bankBranchService.getByBranchNumberAndBank(requireNotNull(accountRequest.bankBranchId), requireNotNull(accountRequest.bankId))

        val currency = currencyService.getByIsoCode(requireNotNull(accountRequest.currencyIsoCode))

        val sequence = accountRepository.findNextAccountNumberSequence()

        val accountNumber = AccountNumberGenerator.generate(accountTypeCode = accountType.code, sequence = sequence)

        val cbu = CBUGenerator.generate(bankBranch.bank.id, requireNotNull(accountRequest.bankBranchId), accountNumber)

        val account = Account(
            type = accountType,
            customer = customer,
            currency = currency,
            bank = bankBranch.bank,
            balance = accountRequest.initialBalance,
            createdBy = user,
            updatedBy = user,
            accountNumber = accountNumber,
            cbu = cbu,
        )

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
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        if (account.status != AccountStatus.PENDING) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID)
        }

        val user = userService.getById(authService.currentUserId)

        account.status = AccountStatus.OPENED
        account.updatedAt = OffsetDateTime.now()
        account.updatedBy = user

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
    fun suspend(id: Long, reason: String) {
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        if (account.status != AccountStatus.OPENED) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID)
        }

        val user: User = userService.getById(SYSTEM_USER)

        account.status = AccountStatus.SUSPENDED
        account.updatedAt = OffsetDateTime.now()
        account.updatedBy = user

        operationLogService.log(
            OperationLogCommand(
                LogOperation.SUSPENSION,
                EntityName.ACCOUNT,
                id,
                reason,
                user,
            ),
        )
    }

    @Transactional
    fun close(id: Long): AccountResponse {
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        if ((account.status != AccountStatus.OPENED) && (account.status != AccountStatus.PENDING)) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID)
        }

        val loans = loanRepository.findAllByAccountId(account.id)

        val loanPending = loans.any { it.status == LoanStatus.ACTIVE }

        if (loanPending) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID_FOR_CLOSURE)
        }

        val user = userService.getById(authService.currentUserId)

        account.status = AccountStatus.CLOSED
        account.updatedAt = OffsetDateTime.now()
        account.updatedBy = user

        operationLogService.log(
            OperationLogCommand(
                LogOperation.SUSPENSION,
                EntityName.ACCOUNT,
                id,
                "Account with id: ${account.id} closed",
                user,
            ),
        )

        return account.toResponse()
    }
}
