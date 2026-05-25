package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerStatus {
  ACTIVE(1),
  INACTIVE(2);

  private final Integer id;
}
