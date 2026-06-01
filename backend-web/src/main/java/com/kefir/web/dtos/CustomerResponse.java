package com.kefir.web.dtos;

import com.kefir.entities.Customer;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record CustomerResponse(
    Long id,
    String name1,
    String name2,
    String name3,
    String lastname1,
    String lastname2,
    String lastname3,
    String personType,
    String documentType,
    String documentNumber,
    String customerType,
    String status,
    String createdByUser,
    OffsetDateTime creationDate) {

  public static CustomerResponse fromEntity(Customer customer) {
    return CustomerResponse.builder()
        .id(customer.getId())
        .name1(customer.getName1())
        .name2(customer.getName2())
        .name3(customer.getName3())
        .lastname1(customer.getLastname1())
        .lastname2(customer.getLastname2())
        .lastname3(customer.getLastname3())
        .personType(customer.getPersonType().getName())
        .documentType(customer.getDocumentType().getName())
        .documentNumber(customer.getDocumentNumber())
        .customerType(customer.getCustomerType().getName())
        .status(customer.getStatus().name())
        .createdByUser(customer.getUser().getUsername())
        .creationDate(customer.getCreatedAt())
        .build();
  }
}
