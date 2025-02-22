package org.kefir.controllers;

import org.kefir.DTOs.CustomerDTO;
import org.kefir.DTOs.LoanDTO;
import org.kefir.entities.Customer;
import org.kefir.entities.Loan;
import org.kefir.services.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Endpoint to retrieve all records from the person_type table
    @GetMapping
    public List<Loan> getAll() {
        return loanService.findAll();
    }

    // Endpoint to retrieve a single record by ID using a JSON request body
    @PostMapping("/getLoanById")
    public Optional<Loan> getById(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        return loanService.findById(id);
    }

    // Endpoint to create a new loan
    @PostMapping("/createLoan")
    public ResponseEntity<Loan> createLoan(@RequestBody LoanDTO loan) {
        Loan createdLoan = loanService.create(loan);
        return ResponseEntity.ok(createdLoan);
    }

    // Endpoint to delete a loan
    @PostMapping("/deleteLoan")
    public ResponseEntity<String> deleteLoan(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        loanService.deleteLoan(id);
        return ResponseEntity.ok("Loan successfully deleted!");
    }

    // Endpoint to delete a loan
    @PostMapping("/updateLoan")
    public ResponseEntity<String> updateLoan(@RequestBody LoanDTO loanDTO) {
        loanService.updateLoan(loanDTO);
        return ResponseEntity.ok("Loan successfully updated!");
    }

}