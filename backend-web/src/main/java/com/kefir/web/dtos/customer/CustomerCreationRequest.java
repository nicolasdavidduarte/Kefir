package com.kefir.web.dtos.customer;

import com.kefir.enums.CustomerType;
import com.kefir.enums.DocumentType;
import com.kefir.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerCreationRequest(
    @NotBlank(message = "Name1 is mandatory") @Size(max = 20) String name1,
    @Size(max = 20) String name2,
    @Size(max = 20) String name3,
    @NotBlank(message = "Lastname1 is mandatory") @Size(max = 20) String lastname1,
    @Size(max = 20) String lastname2,
    @Size(max = 20) String lastname3,
    @NotNull(message = "Person type is mandatory") PersonType personType,
    @NotNull(message = "Document type is mandatory") DocumentType documentType,
    @NotNull(message = "Document number is mandatory") @Size(max = 20) String documentNumber,
    @NotNull(message = "Customer type is mandatory") CustomerType customerType,
    @Email @Size(max = 50) String email) {}
