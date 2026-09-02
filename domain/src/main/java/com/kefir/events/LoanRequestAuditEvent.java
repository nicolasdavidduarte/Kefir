package com.kefir.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanRequestAuditEvent(
    UUID requestId, Long customerId, BigDecimal requestedAmount, Instant timestamp) {}
