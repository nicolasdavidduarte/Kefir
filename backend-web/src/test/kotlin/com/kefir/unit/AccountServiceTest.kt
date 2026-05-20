package com.kefir.unit

import com.kefir.repositories.AccountRepository
import com.kefir.services.AccountService
import com.kefir.services.ApprovalLogService
import com.kefir.services.AuxAuthService
import io.mockk.mockk
import org.junit.jupiter.api.Test

class AccountServiceTest() {

    private val accountRepository: AccountRepository = mockk<AccountRepository>()
    private val auxAuthService = mockk<AuxAuthService>()
    private val approvalLogService = mockk<ApprovalLogService>()
    private val accountService = AccountService(accountRepository, auxAuthService, approvalLogService)

    @Test
    fun testGenerateCBUFirstBlock() {
       val cbuFB = accountService.generateCBUFirstBlock(bank = 321L, branch = 999L)

        assert(cbuFB.contains("3210999"))
    }

    @Test
    fun generateCBUSecondBlock() {
    }

}