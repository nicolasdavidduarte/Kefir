package com.kefir.web.dtos.user;

import com.kefir.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UserRequest(
    @NotBlank @Size(max = 10) String username,
    @NotBlank
        @Pattern(
            regexp =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#\\-])[A-Za-z\\d@$!%*?&._#\\-]{8,255}$",
            message =
                "Password must be 8-64 characters long and contain at least one uppercase letter,"
                    + " one lowercase letter, one number and one special character.")
        String password,
    @NotBlank @Size(max = 50) String fullname,
    List<UserRoles> roles) {}
