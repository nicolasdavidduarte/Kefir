package com.kefir.web.controllers;

import com.kefir.entities.Customer;
import com.kefir.services.CustomerService;
import com.kefir.web.dtos.CustomerDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  // Endpoint to retrieve all records from the core_user table
  @GetMapping
  public List<Customer> getAll() {
    return customerService.findAll();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public Customer getById(@PathVariable Long id) {
    return customerService.findById(id);
  }

  // Create a new customer
  @PostMapping
  public ResponseEntity<Customer> createCustomer(@RequestBody @Valid CustomerDTO customer) {
    return ResponseEntity.ok(customerService.createCustomer(customer));
  }

  // Endpoint to delete a loan
  @DeleteMapping("/deleteCustomer")
  public ResponseEntity<String> deleteCustomer(@RequestBody Map<String, Long> request) {
    Long id = request.get("id");
    customerService.deleteCustomer(id);
    log.info("Customer successfully deleted with id:{}", id);
    return ResponseEntity.ok("Customer successfully deleted!");
  }
}
