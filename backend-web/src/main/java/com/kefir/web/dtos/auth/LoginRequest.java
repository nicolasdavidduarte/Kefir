package com.kefir.web.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank @Size(max = 10) String username, @NotBlank @Size(max = 255) String password) {}
