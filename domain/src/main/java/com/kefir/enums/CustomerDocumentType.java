package com.kefir.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CustomerDocumentType {
  DNI(1),
  PASSPORT(2);

  private final Integer id;
}
