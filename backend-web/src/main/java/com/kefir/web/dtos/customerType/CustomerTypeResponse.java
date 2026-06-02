package com.kefir.web.dtos.customerType;

import com.kefir.entities.CustomerType;

import java.time.OffsetDateTime;

public record CustomerTypeResponse(
        Integer id,
        String name,
        String description,
        Boolean enabled,
        String user,
        OffsetDateTime createdAt
) {
    public static CustomerTypeResponse fromEntity(CustomerType customerType){
        return new CustomerTypeResponse(
                customerType.getId(),
                customerType.getName(),
                customerType.getDescription(),
                customerType.isEnabled(),
                customerType.getUserId().getUsername(),
                customerType.getCreatedAt()
        );
    }
}
