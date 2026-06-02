package com.kefir.web.dtos.common;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
    String error, Object message, int status, OffsetDateTime timestamp) {}
