package com.kefir.web.controllers;

import com.kefir.services.CustomerTypeService;
import com.kefir.web.dtos.customerType.CustomerTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  @ResponseStatus(HttpStatus.OK)
  public List<CustomerTypeResponse> getAll() {
    return customerTypeService.getAllWithResponse();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerTypeResponse getById(@PathVariable Integer id) {
    return customerTypeService.getByIdWithResponse(id);
  }
}
