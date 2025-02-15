package org.kefir.controllers;

import org.kefir.entities.Loan;
import org.kefir.services.LoanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    // Endpoint to retrieve a single record by ID
    @GetMapping("/{id}")
    public Optional<Loan> getById(@PathVariable Long id) {
        return loanService.findById(id);
    }
}