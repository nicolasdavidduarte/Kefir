package com.kefir.web.controllers

import com.kefir.services.loanInstallment.LoanInstallmentService
import com.kefir.web.dtos.LoanInstallmentResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/loans/{loanId}/installments")
class LoanInstallmentController(
    val loanInstallmentService: LoanInstallmentService,
) {

    @GetMapping
    fun getAllInstallments(@PathVariable loanId: Long): List<LoanInstallmentResponse> = loanInstallmentService.getAllInstallments(loanId)
}
