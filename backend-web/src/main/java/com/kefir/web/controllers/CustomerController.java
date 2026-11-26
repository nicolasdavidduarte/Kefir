package com.kefir.web.controllers;

import com.kefir.services.customer.CustomerCommandService;
import com.kefir.services.customer.CustomerQueryService;
import com.kefir.web.dtos.customer.CustomerCreationRequest;
import com.kefir.web.dtos.customer.CustomerResponse;
import com.kefir.web.dtos.customer.CustomerUpdateRequest;
import com.kefir.web.utils.PageResponse;
import com.kefir.web.utils.Pagination;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers services", description = "APIs for customers")
public class CustomerController {

  private final CustomerCommandService customerCommandService;
  private final CustomerQueryService customerQueryService;

  public CustomerController(
      CustomerCommandService customerCommandService, CustomerQueryService customerQueryService) {
    this.customerCommandService = customerCommandService;
    this.customerQueryService = customerQueryService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public PageResponse<CustomerResponse> getAll(
      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
    return customerQueryService.getAllWithResponse(Pagination.from(page, size));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CustomerResponse getById(@PathVariable Long id) {
    return customerQueryService.getByIdWithResponse(id);
  }

  @GetMapping("/search/{query}")
  @ResponseStatus(HttpStatus.OK)
  public List<CustomerResponse> getAllByFullname(@PathVariable String query) {
    return customerQueryService.getAllByFullnameOrDocumentNumber(query);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse create(@RequestBody @Valid CustomerCreationRequest customer) {

    return customerCommandService.create(customer);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse update(
      @RequestBody @Valid CustomerUpdateRequest request, @PathVariable Long id) {
    return customerCommandService.update(request, id);
  }

  @PostMapping("/{id}/status/activate")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse activate(@PathVariable Long id) {

    return customerCommandService.activate(id);
  }

  @PostMapping("/{id}/status/deactivate")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  public CustomerResponse deactivate(@PathVariable Long id) {

    return customerCommandService.deactivate(id);
  }
}
