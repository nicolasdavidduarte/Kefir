package com.kefir.web.controllers

import com.kefir.services.BankService
import com.kefir.web.dtos.BankRequest
import com.kefir.web.dtos.BankResponse
import com.kefir.web.dtos.EntityOperationResponse
import jakarta.validation.Valid
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
    fun getAllBanks(): List<BankResponse> = bankService.findAll()

    @PostMapping
    fun createBank(
        @RequestBody @Valid bankRequest: BankRequest,
    ) = bankService.create(bankRequest)

    @PutMapping("/{id}")
    fun enableBank(
        @PathVariable id: Long,
    ): EntityOperationResponse = bankService.enable(id)
}
