package com.kefir.web.dtos.currency;

import com.kefir.entities.Currency;
import java.time.OffsetDateTime;

public record CurrencyResponse(
    Integer id,
    String isoCode,
    String description,
    Boolean enabled,
    String createdBy,
    OffsetDateTime createdAt,
    String updatedBy,
    OffsetDateTime updatedAt) {
  public static CurrencyResponse fromEntity(Currency currency) {
    return new CurrencyResponse(
        currency.getId(),
        currency.getIsoCode(),
        currency.getDescription(),
        currency.isEnabled(),
        currency.getCreatedBy().getUsername(),
        currency.getCreatedAt(),
        currency.getUpdatedBy().getUsername(),
        currency.getUpdatedAt());
  }
}
