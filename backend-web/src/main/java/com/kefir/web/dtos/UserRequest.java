package com.kefir.web.dtos;

import com.kefir.entities.CoreUser;
import com.kefir.entities.Role;

import java.time.OffsetDateTime;
import java.util.List;

public record UserResponse(
    Integer id,
    String username,
    Boolean enabled,
    OffsetDateTime createdAt,
    List<String> roles
) {
    public static UserResponse fromEntity(CoreUser user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}
