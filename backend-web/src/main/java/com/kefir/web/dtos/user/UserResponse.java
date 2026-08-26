package com.kefir.web.dtos.user;

import com.kefir.entities.Role;
import com.kefir.entities.User;
import java.time.OffsetDateTime;
import java.util.List;

public record UserResponse(
    Integer id,
    String username,
    Boolean enabled,
    String createdBy,
    OffsetDateTime createdAt,
    List<String> roles) {
  public static UserResponse fromEntity(User user) {
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.isEnabled(),
        user.getCreatedBy().getUsername(),
        user.getCreatedAt(),
        user.getRoles().stream().map(Role::getName).toList());
  }
}
