package com.kefir.web.controllers;

import com.kefir.entities.CustomerType;
import com.kefir.services.CustomerTypeService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customerType")
public class CustomerTypeController {

  private final CustomerTypeService customerTypeService;

  public CustomerTypeController(CustomerTypeService customerTypeService) {
    this.customerTypeService = customerTypeService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public List<CustomerType> getAll() {
    return customerTypeService.findAll();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public Optional<CustomerType> getById(@PathVariable Long id) {
    return customerTypeService.findById(id);
  }
}
