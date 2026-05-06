package com.kefir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerStatus {
  ACTIVE(1),
  INACTIVE(2);

  private final Integer id;
}
