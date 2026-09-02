package com.kefir.events;

import java.util.UUID;

public record AccountDeactivationEvent(UUID requestId, Long accountId, String reason) {}
