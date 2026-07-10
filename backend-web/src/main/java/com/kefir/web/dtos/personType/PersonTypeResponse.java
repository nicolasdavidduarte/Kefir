package com.kefir.web.dtos.personType;

import com.kefir.entities.PersonType;
import java.time.OffsetDateTime;

public record PersonTypeResponse(
    Integer id,
    String name,
    String description,
    Boolean enabled,
    String createdBy,
    OffsetDateTime createdAt,
    String updatedBy,
    OffsetDateTime updatedAt) {
  public static PersonTypeResponse fromEntity(PersonType personType) {
    return new PersonTypeResponse(
        personType.getId(),
        personType.getName(),
        personType.getDescription(),
        personType.isEnabled(),
        personType.getCreatedBy().getUsername(),
        personType.getCreatedAt(),
        personType.getUpdatedBy().getUsername(),
        personType.getUpdatedAt());
  }
}
