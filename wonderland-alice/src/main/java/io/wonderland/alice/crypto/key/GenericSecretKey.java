package io.wonderland.alice.crypto.key;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.exception.ExceptionMessages;
import javax.crypto.SecretKey;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;


@ToString
public final class GenericSecretKey implements SecretKey {

  private final byte[] value;

  public GenericSecretKey(byte[] arg) {
    if (ArrayUtils.isEmpty(arg)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    this.value = new byte[arg.length];
    System.arraycopy(arg, 0, this.value, 0, arg.length);
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.GENERIC.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.GENERIC.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return value;
  }
}
