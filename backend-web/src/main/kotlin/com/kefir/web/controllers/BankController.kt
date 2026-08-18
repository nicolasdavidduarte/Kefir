package com.kefir.web.controllers

import com.kefir.services.BankService
import com.kefir.web.dtos.bank.BankRequest
import com.kefir.web.dtos.bank.BankResponse
import com.kefir.web.dtos.entityOperation.EntityOperationResponse
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/banks")
@RestController
class BankController(
    val bankService: BankService,
) {
    @GetMapping
    fun getAllBanks(): List<BankResponse> = bankService.getAll()

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPR')")
    fun createBank(
        @RequestBody @Valid bankRequest: BankRequest,
    ) = bankService.create(bankRequest)

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPR')")
    fun activateBank(
        @PathVariable id: Int,
    ): EntityOperationResponse = bankService.enable(id)
}
