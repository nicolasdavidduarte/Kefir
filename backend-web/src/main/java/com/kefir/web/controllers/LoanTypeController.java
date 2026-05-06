package com.kefir.web.controllers;

import com.kefir.entities.LoanType;
import com.kefir.services.LoanTypeService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loanType")
public class LoanTypeController {

  private final LoanTypeService loanTypeService;

  public LoanTypeController(LoanTypeService loanTypeService) {
    this.loanTypeService = loanTypeService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public List<LoanType> getAll() {
    return loanTypeService.findAll();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public Optional<LoanType> getById(@PathVariable Long id) {
    return loanTypeService.findById(id);
  }
}
