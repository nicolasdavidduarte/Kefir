package com.kefir.web.dtos.customer;

import com.kefir.entities.Customer;
import java.time.OffsetDateTime;

public record CustomerResponse(
    Long id,
    String name1,
    String name2,
    String name3,
    String lastname1,
    String lastname2,
    String lastname3,
    String fullname,
    String personType,
    String documentType,
    String documentNumber,
    String customerType,
    String email,
    String status,
    String createdBy,
    OffsetDateTime createdAt,
    String updatedBy,
    OffsetDateTime updatedAt) {

  public static CustomerResponse fromEntity(Customer customer) {
    return new CustomerResponse(
        customer.getId(),
        customer.getName1(),
        customer.getName2(),
        customer.getName3(),
        customer.getLastname1(),
        customer.getLastname2(),
        customer.getLastname3(),
        customer.getFullname(),
        customer.getPersonType().getName(),
        customer.getDocumentType().getName(),
        customer.getDocumentNumber(),
        customer.getCustomerType().getName(),
        customer.getEmail(),
        customer.getStatus().name(),
        customer.getCreatedBy().getUsername(),
        customer.getCreatedAt(),
        customer.getUpdatedBy().getUsername(),
        customer.getUpdatedAt());
  }
}
