package com.kefir.services

import com.kefir.entities.Account
import com.kefir.entities.close
import com.kefir.entities.open
import com.kefir.exceptions.AccountNotFoundException
import com.kefir.repositories.AccountRepository
import com.kefir.web.dtos.AccountRequest
import com.kefir.web.dtos.AccountResponse
import com.kefir.web.dtos.ApprovalLogRequest
import com.kefir.web.dtos.EntityApprovalRequest
import com.kefir.web.dtos.EntityApprovalResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
        val cbu = generateCBUFirstBlock(requireNotNull(accountRequest.bank), requireNotNull(accountRequest.bankBranch))

        val savedAccount =
            accountRepository.save(
                Account(
                    type = requireNotNull(accountRequest.type),
                    customer = requireNotNull(accountRequest.customer),
                    currency = requireNotNull(accountRequest.currency),
                    cbu = cbu,
                    bank = requireNotNull(accountRequest.bank),
                    balance = requireNotNull(accountRequest.initialBalance),
                ),
            )

        savedAccount.cbu = generateCBUSecondBlock(savedAccount.cbu, savedAccount.id)

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

    fun generateCBUFirstBlock(
        bank: Long,
        branch: Long,
    ): String {
        // 1st block: Bank code (3) + Branch code (4) + Verification number (1)

        val cbu: String =
            bank.toString().padStart(3, '0') +
                branch.toString().padStart(4, '0') +
                (0..9).random().toString()

        return cbu
    }

    fun generateCBUSecondBlock(
        cbuFirstBlock: String,
        id: Long,
    ): String {
        // 2nd block: Account number (13) + Verification number (1)

        val cbu =
            cbuFirstBlock +
                id.toString().padStart(13, '0') +
                (0..9).random().toString()

        return cbu
    }
}
