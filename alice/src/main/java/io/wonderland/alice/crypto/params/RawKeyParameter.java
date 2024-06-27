package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameter;
import java.util.Objects;
import lombok.Getter;

@Getter
public class RawKeyParameter implements CipherParameter {

  private final byte[] key;

  public RawKeyParameter(byte[] key) {
    this(key, 0, key.length);
  }

  public RawKeyParameter(byte[] key, int keyOffset, int keyLen) {
    Objects.requireNonNull(key, "Key encoded must not be null.");
    this.key = new byte[keyLen];
    System.arraycopy(key, keyOffset, this.key, 0, keyLen);
  }

}
