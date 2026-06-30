package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoanStatus {
  ACTIVE,
  INACTIVE,
  PENDING,
  CHARGE_OFF
}
