package com.kefir.web.dtos;

import java.time.OffsetDateTime;

public record AuthResponse(String accessToken, OffsetDateTime createdAt) {}
