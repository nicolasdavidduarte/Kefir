package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyIsoCodes {
  ACTIVE,
  INACTIVE,
  PENDING,
  CLOSED
}
