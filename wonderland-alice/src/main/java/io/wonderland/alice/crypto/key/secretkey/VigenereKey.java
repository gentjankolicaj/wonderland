package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.VigenereKeyASN1Codec;
import io.wonderland.alice.exception.ExceptionMessages;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@ToString
public final class VigenereKey implements SecretKey {

  private static final Function<VigenereKey, byte[]> ENCODER = VigenereKeyASN1Codec.getInstance()
      .encoder();

  private final int modulus;
  private final byte[] key;

  public VigenereKey(int modulus, byte... key) {
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

  public VigenereKey(byte[] key) {
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_ARGS_NOT_VALID);
    }
    this.modulus = CharsetsUtils.getDefaultAlphabetSize();
    this.key = new byte[key.length];
    System.arraycopy(key, 0, this.key, 0, key.length);
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.VIGENERE.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.VIGENERE.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return ENCODER.apply(this);
  }
}
