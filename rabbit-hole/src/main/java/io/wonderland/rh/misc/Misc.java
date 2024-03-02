package io.wonderland.rh.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Misc {
  ENCODING("Encoding"),BASE_CONVERTER("Base converter");
  private final String label;

}
