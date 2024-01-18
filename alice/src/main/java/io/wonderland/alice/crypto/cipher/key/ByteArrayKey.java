package io.wonderland.alice.crypto.cipher.key;

import io.wonderland.alice.crypto.cipher.ExceptionMessages;
import java.security.Key;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;


@ToString
public final class ByteArrayKey implements Key {

  private final byte[] key;

  public ByteArrayKey(byte[] key) {
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    this.key = new byte[key.length];
    System.arraycopy(key, 0, this.key, 0, key.length);
  }

  @Override
  public String getAlgorithm() {
    return "raw";
  }

  @Override
  public String getFormat() {
    return KeyFormats.BYTE_ARRAY.name();
  }

  @Override
  public byte[] getEncoded() {
    return key;
  }
}
