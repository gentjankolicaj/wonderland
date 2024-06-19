package io.wonderland.alice.crypto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Algorithms {

  CAESAR("Caesar", KeyFormat.INT), OTP("OTP", KeyFormat.BYTES),
  AFFINE("Affine", KeyFormat.INT),
  NULL("Null", KeyFormat.NULL), EMPTY("Empty", KeyFormat.EMPTY),
  GENERIC("Generic", KeyFormat.BYTES),
  RAILFENCE("Railfence", KeyFormat.INT), HILL("Hill", KeyFormat.BYTES),
  VIGENERE("Vigenere", KeyFormat.BYTES), VERNAM("Vernam", KeyFormat.BYTES),
  MONOALPHABET("Monoalphabet", KeyFormat.INT_MAP),
  PERMUTATION("Permutation", KeyFormat.INTS),
  RSA("RSA", KeyFormat.BYTES),
  AES("AES", KeyFormat.BYTES),
  DES("DES", KeyFormat.BYTES);

  private final String name;
  private final KeyFormat keyFormat;

}
