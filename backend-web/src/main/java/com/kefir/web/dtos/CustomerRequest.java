package com.kefir.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
    @NotBlank(message = "Name1 is mandatory") String name1,
    String name2,
    String name3,
    @NotBlank(message = "Lastname1 is mandatory") String lastname1,
    String lastname2,
    String lastname3,
    @NotNull(message = "Person type is mandatory") String personType,
    @NotNull(message = "Document type is mandatory") String documentType,
    @NotNull(message = "Document number is mandatory") String documentNumber,
    @NotNull(message = "Customer type is mandatory") String customerType) {}
