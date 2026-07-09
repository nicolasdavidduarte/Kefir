package com.kefir.web.controllers

import com.kefir.services.loanInstallment.LoanInstallmentService
import com.kefir.web.dtos.LoanInstallmentResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/loans/{loanId}/installments")
class LoanInstallmentController(
    val loanInstallmentService: LoanInstallmentService,
) {

    @GetMapping
    fun getAllInstallments(@PathVariable loanId: Long): List<LoanInstallmentResponse> = loanInstallmentService.getAllInstallments(loanId)

    @PostMapping("/{installmentNumber}/payment")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','OPR')")
    fun createPayment(@PathVariable loanId: Long, @PathVariable installmentNumber: Int): LoanInstallmentResponse = loanInstallmentService.payInstallment(loanId, installmentNumber)
}
