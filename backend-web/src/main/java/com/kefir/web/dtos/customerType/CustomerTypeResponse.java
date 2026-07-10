package com.kefir.web.dtos.customerType;

import com.kefir.entities.CustomerType;
import java.time.OffsetDateTime;

public record CustomerTypeResponse(
    Integer id,
    String name,
    String description,
    Boolean enabled,
    String createdBy,
    OffsetDateTime createdAt,
    String updatedBy,
    OffsetDateTime updatedAt) {
  public static CustomerTypeResponse fromEntity(CustomerType customerType) {
    return new CustomerTypeResponse(
        customerType.getId(),
        customerType.getName(),
        customerType.getDescription(),
        customerType.isEnabled(),
        customerType.getCreatedBy().getUsername(),
        customerType.getCreatedAt(),
        customerType.getUpdatedBy().getUsername(),
        customerType.getUpdatedAt());
  }
}
