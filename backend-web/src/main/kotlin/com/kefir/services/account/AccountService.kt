package com.kefir.services.account

import com.kefir.entities.Account
import com.kefir.entities.AccountType
import com.kefir.entities.BankBranch
import com.kefir.entities.Currency
import com.kefir.entities.Customer
import com.kefir.entities.User
import com.kefir.entities.close
import com.kefir.entities.open
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
import com.kefir.web.dtos.AccountRequest
import com.kefir.web.dtos.AccountResponse
import com.kefir.web.dtos.OperationLogCommand
import com.kefir.web.dtos.toResponse
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

    /**
     * Obtains all accounts from the database
     * @return list of accounts
     */
    @Transactional(readOnly = true)
    fun getAllAccounts(): List<AccountResponse> = accountRepository.findAllByOrderByIdAsc().map(Account::toResponse).toList()

    @Transactional(readOnly = true)
    fun getById(id: Long): Account = accountRepository.findById(id).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

    @Transactional(readOnly = true)
    fun getByIdWithResponse(id: Long): AccountResponse {
        val account = accountRepository.findById(id).orElseThrow { ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        return account.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllByCustomerWithResponse(customerId: Long): List<AccountResponse> = accountRepository.findAllByCustomerId(customerId).map(Account::toResponse)

    @Transactional
    fun addBalance(account: Account, amount: BigDecimal) {
        account.balance += amount
        accountRepository.save(account)
    }

    @Transactional
    fun subtractBalance(account: Account, amount: BigDecimal) {
        account.balance -= amount
        accountRepository.save(account)
    }

    /**
     * Creates a new account
     * @param [accountRequest] account request
     * @return account response
     */
    @Transactional
    fun createAccount(accountRequest: AccountRequest): AccountResponse {
        val customer: Customer = customerService.getById(accountRequest.customerId)

        if (customer.status != CustomerStatus.ACTIVE) {
            throw ApiException(ErrorCode.CUSTOMER_NOT_VALID)
        }

        val user: User = userService.getById(authService.currentUserId)

        val accountType: AccountType = accountTypeService.getByName(accountRequest.type?.dbName)

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
                    createdBy = user,
                    updatedBy = user,
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

    @Transactional
    fun open(id: Long): AccountResponse {
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.LOAN_NOT_FOUND) }

        account.open()
        account.updatedAt = OffsetDateTime.now()

        return accountRepository.save(account).toResponse()
    }

    @Transactional
    fun close(id: Long): AccountResponse {
        val account = accountRepository.findById(id).orElseThrow { throw ApiException(ErrorCode.ACCOUNT_NOT_FOUND) }

        val loans = loanRepository.findAllByAccountId(account.id)

        val loanPending = loans.any { it.status == LoanStatus.ACTIVE }

        if (loanPending) {
            throw ApiException(ErrorCode.ACCOUNT_NOT_VALID_FOR_CLOSURE)
        }

        account.close()
        account.updatedAt = OffsetDateTime.now()

        return accountRepository.save(account).toResponse()
    }
}
