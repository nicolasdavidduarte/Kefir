package com.kefir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoreUserUsername {
  ADMIN(1);

  private final Integer code;
}
