package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoanStatus {
  ACTIVE,
  CLOSED,
  PENDING,
  CHARGE_OFF
}
