package com.kefir.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {
  DNI(1),
  PASSPORT(2);

  private final Integer id;
}
