package com.kefir.web.controllers

import com.kefir.entities.LoanType
import com.kefir.services.LoanTypeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/loans")
class LoanTypeController(private val loanTypeService: LoanTypeService) {

    @GetMapping("/types")
    fun getLoanTypes(): List<LoanType> = loanTypeService.getAllLoanTypes()
}