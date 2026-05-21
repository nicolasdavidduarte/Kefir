package com.kefir.services

import com.kefir.entities.Account
import com.kefir.entities.close
import com.kefir.entities.open
import com.kefir.exceptions.AccountNotFoundException
import com.kefir.repositories.AccountRepository
import com.kefir.services.aux.account.CBUGenerator
import com.kefir.web.dtos.AccountRequest
import com.kefir.web.dtos.AccountResponse
import com.kefir.web.dtos.ApprovalLogRequest
import com.kefir.web.dtos.EntityApprovalRequest
import com.kefir.web.dtos.EntityApprovalResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.Long

@Transactional
@Service
class AccountService(
    val accountRepository: AccountRepository,
    val auxAuthService: AuxAuthService,
    val approvalLogService: ApprovalLogService,
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
     * @param account request
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

        savedAccount.cbu = CBUGenerator.generateCBU(savedAccount.bank, requireNotNull(accountRequest.bankBranch), savedAccount.id)

        return accountRepository.save(savedAccount).toResponse()
    }

    fun approve(
        id: Long,
        request: EntityApprovalRequest,
    ): EntityApprovalResponse {
        val account = accountRepository.findById(id).orElseThrow { throw AccountNotFoundException("Account not found") }

        if (request.approve!!) account.open() else account.close()
        accountRepository.save(account)

        val user: Int? = auxAuthService.retrieveUserIdFromAuth()

        val approvalLogRequest =
            ApprovalLogRequest(
                entity = "ACCOUNT",
                approvableId = id,
                comments = request.comments,
                status = account.status,
                actionedBy = user?.toLong(),
            )

        approvalLogService.create(approvalLogRequest)

        return EntityApprovalResponse("Account opened!", "Account", id, LocalDateTime.now())
    }
}
