package com.kefir.web.controllers;

import com.kefir.services.CustomerService;
import com.kefir.web.dtos.customer.CustomerCreationRequest;
import com.kefir.web.dtos.customer.CustomerResponse;
import com.kefir.web.dtos.customer.CustomerUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers services", description = "APIs for customers")
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
      @RequestBody @Valid CustomerCreationRequest customer) {
    return ResponseEntity.ok(customerService.create(customer));
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public ResponseEntity<CustomerResponse> updateCustomer(
      @RequestBody @Valid CustomerUpdateRequest request, @PathVariable Long id) {
    return ResponseEntity.ok(customerService.update(request, id));
  }

  // Endpoint to delete a loan
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {

    customerService.delete(id);

    return ResponseEntity.ok("Customer successfully deleted!");
  }

  @PostMapping("/{id}/status")
  public ResponseEntity<Map<String, String>> activateCustomer(@PathVariable Long id) {
    customerService.activate(id);

    return ResponseEntity.ok(Map.of("Message", "Customer successfully activated"));
  }
}
