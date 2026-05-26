package com.kefir.web.dtos;

import com.kefir.entities.CustomerType;
import com.kefir.entities.DocumentType;
import com.kefir.entities.PersonType;
import com.kefir.enums.CustomerDocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerDTO(
    @NotBlank(message = "Name1 is mandatory") String name1,
    String name2,
    String name3,
    @NotBlank(message = "Lastname1 is mandatory") String lastname1,
    String lastname2,
    String lastname3,
    @NotNull(message = "Person type is mandatory") PersonType personType,
    @NotNull(message = "Document type is mandatory") String documentType,
    @NotNull(message = "Document number is mandatory") String documentNumber,
    @NotNull(message = "Customer type is mandatory") CustomerType customerType) {}
