package com.kefir.web.dtos.customer;

import com.kefir.enums.CustomerType;
import com.kefir.enums.DocumentType;
import com.kefir.enums.PersonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerCreationRequest(
    @NotBlank(message = "Name1 is mandatory") String name1,
    String name2,
    String name3,
    @NotBlank(message = "Lastname1 is mandatory") String lastname1,
    String lastname2,
    String lastname3,
    @NotNull(message = "Person type is mandatory") PersonType personType,
    @NotNull(message = "Document type is mandatory") DocumentType documentType,
    @NotNull(message = "Document number is mandatory") String documentNumber,
    @NotNull(message = "Customer type is mandatory") CustomerType customerType) {}
