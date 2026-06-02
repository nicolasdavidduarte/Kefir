package com.kefir.web.dtos.auth;

import java.time.OffsetDateTime;

public record AuthResponse(String accessToken, OffsetDateTime createdAt) {}
