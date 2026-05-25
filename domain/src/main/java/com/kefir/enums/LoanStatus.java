package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoanStatus {
  ACTIVE(1),
  INACTIVE(2),
  PENDING(3),
  CLOSED(4);

  private final Integer id;
}
