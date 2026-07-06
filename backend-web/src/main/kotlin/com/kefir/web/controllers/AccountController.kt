package com.kefir.web.controllers

import com.kefir.services.account.AccountService
import com.kefir.web.dtos.AccountRequest
import com.kefir.web.dtos.AccountResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    val accountService: AccountService,
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllAccounts(): List<AccountResponse> = accountService.getAllAccounts().sortedBy { a -> a.id }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: Long): AccountResponse = accountService.getByIdWithResponse(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','OPR')")
    fun createAccount(
        @RequestBody @Valid accountRequest: AccountRequest,
    ) = accountService.createAccount(accountRequest)

    @PatchMapping("/{id}/open")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','OPR')")
    fun openAccount(
        @PathVariable id: Long,
    ) = accountService.open(id)

    @PatchMapping("/{id}/close")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','OPR')")
    fun closeAccount(
        @PathVariable id: Long,
    ) = accountService.close(id)
}
