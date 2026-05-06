package com.kefir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdempotencyState {
  PROCESSING,
  COMPLETED,
  FAILED;
}
