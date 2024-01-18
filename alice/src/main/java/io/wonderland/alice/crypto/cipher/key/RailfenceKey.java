package io.wonderland.alice.crypto.cipher.key;

import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.common.PrimitiveUtils;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public final class RailfenceKey implements SecretKey {

  private final int rails;

  @Override
  public String getAlgorithm() {
    return AlgNames.Railfence;
  }

  @Override
  public String getFormat() {
    return KeyFormats.INT.name();
  }

  @Override
  public byte[] getEncoded() {
    return PrimitiveUtils.getBytes(rails);
  }
}
