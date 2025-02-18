package org.kefir.controllers;

import org.kefir.DTOs.CustomerDTO;
import org.kefir.entities.Customer;
import org.kefir.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
}