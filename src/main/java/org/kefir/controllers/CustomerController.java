package org.kefir.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.kefir.DTOs.CustomerDTO;
import org.kefir.entities.Customer;
import org.kefir.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public Optional<Customer> getById(@PathVariable Long id) {
    return customerService.findById(id);
  }

  // Create a new customer
  @PostMapping
  public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDTO customer) {
    Customer createdCustomer = customerService.createCustomer(customer);
    return ResponseEntity.ok(createdCustomer);
  }

  // Endpoint to delete a loan
  @DeleteMapping("/deleteCustomer")
  public ResponseEntity<String> deleteCustomer(@RequestBody Map<String, Long> request) {
    Long id = request.get("id");
    customerService.deleteCustomer(id);
    return ResponseEntity.ok("Customer successfully deleted!");
  }
}
