package io.wonderland.alice.crypto.cipher.key;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KeyFormats {
  STRING("string"), BYTE_ARRAY("byte_array"), INT_ARRAY("int_array"), INT("int"),
  OBJECT_MAP("object_map"), INT_MAP("int_map");
  private final String name;
}
