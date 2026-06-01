package com.kefir.web.controllers;

import com.kefir.services.CustomerService;
import com.kefir.web.dtos.CustomerRequest;
import com.kefir.web.dtos.CustomerResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  // Endpoint to retrieve all records from the core_user table
  @GetMapping
  public List<CustomerResponse> getAll() {
    return customerService.getAllWithResponse();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public CustomerResponse getById(@PathVariable Long id) {
    return customerService.getByIdWithResponse(id);
  }

  // Create a new customer
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public ResponseEntity<CustomerResponse> createCustomer(
      @RequestBody @Valid CustomerRequest customer) {
    return ResponseEntity.ok(customerService.createCustomer(customer));
  }

  // Endpoint to delete a loan
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {

    customerService.deleteCustomer(id);

    return ResponseEntity.ok("Customer successfully deleted!");
  }
}
