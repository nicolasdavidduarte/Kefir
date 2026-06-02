package com.kefir.web.dtos.personType;

import com.kefir.entities.PersonType;
import java.time.OffsetDateTime;

public record PersonTypeResponse(
    Integer id,
    String name,
    String description,
    Boolean enabled,
    String user,
    OffsetDateTime createdAt) {
  public static PersonTypeResponse fromEntity(PersonType personType) {
    return new PersonTypeResponse(
        personType.getId(),
        personType.getName(),
        personType.getDescription(),
        personType.isEnabled(),
        personType.getUserId().getUsername(),
        personType.getCreatedAt());
  }
}
