package com.kefir.web.controllers

import com.kefir.entities.LoanType
import com.kefir.services.LoanTypeService
import com.kefir.web.DTOs.LoanTypeRequest
import com.kefir.web.DTOs.LoanTypeResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/loans")
class LoanTypeController(private val loanTypeService: LoanTypeService) {

    @GetMapping("/types")
    fun getLoanTypes(): List<LoanType> = loanTypeService.getAllLoanTypes()

    @PostMapping("/types")
    @ResponseStatus(HttpStatus.CREATED)
    fun createLoanType(@RequestBody @Valid loanTypeRequest : LoanTypeRequest) : LoanTypeResponse =
         loanTypeService.create(loanTypeRequest)

}