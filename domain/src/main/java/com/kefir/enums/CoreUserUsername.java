package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoreUserUsername {
  ADMIN(1);

  private final Integer code;
}
