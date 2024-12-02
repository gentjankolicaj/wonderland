package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.VernamKeyASN1Codec;
import io.wonderland.alice.exception.ExceptionMessages;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;


@Getter
@ToString
public final class VernamKey implements SecretKey {

  private static final Function<VernamKey, byte[]> ENCODER = VernamKeyASN1Codec.getInstance()
      .encoder();

  private final int modulus;
  private final byte[] key;

  public VernamKey(int modulus, byte... key) {
    if (modulus <= 0) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_MODULUS_NOT_VALID);
    }
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_ARGS_NOT_VALID);
    }
    this.modulus = modulus;
    this.key = new byte[key.length];
    System.arraycopy(key, 0, this.key, 0, key.length);
  }

  public VernamKey(byte[] key) {
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_ARGS_NOT_VALID);
    }
    this.modulus = CharsetsUtils.getDefaultAlphabetSize();
    this.key = new byte[key.length];
    System.arraycopy(key, 0, this.key, 0, key.length);
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.VERNAM.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.VERNAM.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return ENCODER.apply(this);
  }
}
