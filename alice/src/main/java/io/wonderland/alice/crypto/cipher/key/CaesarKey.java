package io.wonderland.alice.crypto.cipher.key;

import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.commons.PrimitiveUtils;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public final class CaesarKey implements SecretKey {

  private final int shift;

  public String getAlgorithm() {
    return AlgNames.CAESAR;
  }

  @Override
  public String getFormat() {
    return KeyFormats.INT.name();
  }

  public byte[] getEncoded() {
    return PrimitiveUtils.getBytes(shift);
  }
}