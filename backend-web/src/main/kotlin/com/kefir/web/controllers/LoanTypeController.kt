package com.kefir.web.controllers

import com.kefir.entities.LoanType
import com.kefir.services.LoanTypeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/loans")
class LoanTypeController(private val loanTypeService: LoanTypeService) {

    @GetMapping("/types")
    fun getLoanTypes(): List<LoanType> = loanTypeService.getAllLoanTypes()

    @PostMapping("/types")
    fun createLoanType(@RequestBody @Valid loanType : LoanType) : ResponseEntity<LoanType> =
         ResponseEntity.ok(loanTypeService.create(loanType))

}