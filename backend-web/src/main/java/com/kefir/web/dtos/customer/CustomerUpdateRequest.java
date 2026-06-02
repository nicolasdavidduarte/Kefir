package com.kefir.web.dtos.customer;

import com.kefir.enums.CustomerType;
import com.kefir.enums.DocumentType;
import com.kefir.enums.PersonType;

public record CustomerUpdateRequest(
    String name1,
    String name2,
    String name3,
    String lastname1,
    String lastname2,
    String lastname3,
    PersonType personType,
    DocumentType documentType,
    String documentNumber,
    CustomerType customerType) {}
