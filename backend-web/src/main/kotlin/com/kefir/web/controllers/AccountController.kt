package com.kefir.web.controllers

import com.kefir.services.AccountService
import com.kefir.web.DTOs.AccountRequest
import com.kefir.web.DTOs.AccountResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accounts")
class AccountController(val accountService: AccountService) {

    @GetMapping
    fun getAllAccounts() : List<AccountResponse> = accountService.getAllAccounts()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestBody @Valid accountRequest: AccountRequest) = accountService.createAccount(accountRequest)
}