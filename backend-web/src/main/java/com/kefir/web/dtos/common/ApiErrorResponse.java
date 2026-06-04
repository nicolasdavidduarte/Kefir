package com.kefir.web.dtos.common;

import java.time.OffsetDateTime;

public record ApiErrorResponse(Object message, int status, OffsetDateTime timestamp) {}
