package com.kefir.logging;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "application_logs")
public record ApplicationLog (
    @Id String id,
    Instant timestamp,
    String level,
    String logger,
    String message,
    String exceptionType
){}
