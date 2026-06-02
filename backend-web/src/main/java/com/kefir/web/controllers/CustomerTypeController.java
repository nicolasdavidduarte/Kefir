package com.kefir.web.controllers;

import com.kefir.entities.CustomerType;
import com.kefir.services.CustomerTypeService;
import com.kefir.web.dtos.customerType.CustomerTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/api/customerTypes")
@Tag(name = "Customer types services", description = "APIs for customer types")
public class CustomerTypeController {

  private final CustomerTypeService customerTypeService;

  public CustomerTypeController(CustomerTypeService customerTypeService) {
    this.customerTypeService = customerTypeService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public ResponseEntity<List<CustomerTypeResponse>> getAll() {
    return ResponseEntity.ok(customerTypeService.getAll());
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public ResponseEntity<CustomerTypeResponse> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(customerTypeService.getById(id));
  }
}
