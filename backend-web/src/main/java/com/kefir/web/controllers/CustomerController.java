package com.kefir.web.controllers;

import com.kefir.entities.Customer;
import com.kefir.services.CustomerService;
import com.kefir.web.dtos.common.ApiEntityResponse;
import com.kefir.web.dtos.customer.CustomerCreationRequest;
import com.kefir.web.dtos.customer.CustomerResponse;
import com.kefir.web.dtos.customer.CustomerUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
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
  @ResponseStatus(HttpStatus.OK)
  public List<CustomerResponse> getAll() {
    return customerService.getAllWithResponse();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerResponse getById(@PathVariable Long id) {
    return customerService.getByIdWithResponse(id);
  }

  // Create a new customer
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse createCustomer(@RequestBody @Valid CustomerCreationRequest customer) {

    return customerService.create(customer);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse updateCustomer(
      @RequestBody @Valid CustomerUpdateRequest request, @PathVariable Long id) {
    return customerService.update(request, id);
  }

  // Endpoint to delete a loan
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ApiEntityResponse deleteCustomer(@PathVariable Long id) {

    customerService.delete(id);

    return new ApiEntityResponse(id, "Customer successfully deleted", OffsetDateTime.now());
  }

  @PostMapping("/{id}/status/activate")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse activateCustomer(@PathVariable Long id) {

    return customerService.activate(id);
  }

  @PostMapping("/{id}/status/deactivate")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse deactivateCustomer(@PathVariable Long id) {

    return customerService.deactivate(id);
  }
}
