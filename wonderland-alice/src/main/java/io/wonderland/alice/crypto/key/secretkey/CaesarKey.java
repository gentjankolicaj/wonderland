package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.CaesarKeyASN1Codec;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public final class CaesarKey implements SecretKey {

  private static final Function<CaesarKey, byte[]> encoder = CaesarKeyASN1Codec.getInstance()
      .encoder();

  private final int shift;

  @Override
  public String getAlgorithm() {
    return Algorithms.CAESAR.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.CAESAR.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return encoder.apply(this);
  }
}