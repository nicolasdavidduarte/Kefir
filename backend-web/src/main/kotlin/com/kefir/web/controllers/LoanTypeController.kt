package com.kefir.web.controllers

import com.kefir.entities.LoanType
import com.kefir.services.LoanTypeService
import com.kefir.web.dtos.LoanTypeRequest
import com.kefir.web.dtos.LoanTypeResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/loans")
class LoanTypeController(
    private val loanTypeService: LoanTypeService,
) {
    @GetMapping("/types")
    fun getLoanTypes(): List<LoanType> = loanTypeService.getAllLoanTypes()

    @PostMapping("/types")
    @ResponseStatus(HttpStatus.CREATED)
    fun createLoanType(
        @RequestBody @Valid loanTypeRequest: LoanTypeRequest,
    ): LoanTypeResponse = loanTypeService.create(loanTypeRequest)
}
