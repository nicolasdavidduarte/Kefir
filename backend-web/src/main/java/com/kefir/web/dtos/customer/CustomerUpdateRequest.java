package com.kefir.web.dtos.customer;

import com.kefir.enums.CustomerType;
import com.kefir.enums.DocumentType;
import com.kefir.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
    @Size(max = 20) String name1,
    @Size(max = 20) String name2,
    @Size(max = 20) String name3,
    @Size(max = 20) String lastname1,
    @Size(max = 20) String lastname2,
    @Size(max = 20) String lastname3,
    PersonType personType,
    DocumentType documentType,
    @Size(max = 20) String documentNumber,
    CustomerType customerType,
    @Email @Size(max = 50) String email) {}
