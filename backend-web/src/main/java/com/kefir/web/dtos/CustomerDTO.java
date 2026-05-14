package com.kefir.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerDTO(
    @NotBlank(message = "Name1 is mandatory") String name1,
    String name2,
    String name3,
    @NotBlank(message = "Lastname1 is mandatory") String lastname1,
    String lastname2,
    String lastname3,
    @NotNull(message = "Person type is mandatory") Integer personType,
    @NotNull(message = "Document type is mandatory") Integer documentType,
    @NotNull(message = "Document number is mandatory") Integer documentNumber,
    @NotNull(message = "Customer type is mandatory") Integer customerType) {}
