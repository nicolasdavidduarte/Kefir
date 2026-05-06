package com.kefir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoanStatus {
  ACTIVE(1),
  INACTIVE(2),
  PENDING(3),
  CLOSED(4);

  private final Integer id;
}
