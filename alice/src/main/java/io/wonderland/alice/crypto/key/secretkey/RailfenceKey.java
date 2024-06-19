package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.RailfenceKeyASN1Codec;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public final class RailfenceKey implements SecretKey {

  private static final Function<RailfenceKey, byte[]> ENCODER = RailfenceKeyASN1Codec.getInstance()
      .encoder();

  private final int rails;

  @Override
  public String getAlgorithm() {
    return Algorithms.RAILFENCE.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.RAILFENCE.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return ENCODER.apply(this);
  }
}
