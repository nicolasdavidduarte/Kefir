package com.kefir.web.dtos.common;

import java.time.OffsetDateTime;

public record ApiEntityResponse(Long resourceId, String message, OffsetDateTime timestamp) {}
