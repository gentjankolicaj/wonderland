package io.wonderland.alice.crypto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum KeyFormat {
  BYTES("bytes"), BYTE("byte"), INT("int"), INTS("ints"), NULL(null), EMPTY(""),
  OBJECT_MAP("object_map"), INT_MAP("int_map");
  private final String value;

}
