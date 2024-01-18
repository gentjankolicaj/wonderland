package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameters;
import java.util.Objects;
import lombok.Getter;

@Getter
public class KeyParameter implements CipherParameters {

  private byte[] key;

  public KeyParameter(byte[] key) {
    this(key, 0, key.length);
  }

  public KeyParameter(byte[] key, int offset, int length) {
    Objects.requireNonNull(key, "Key encoded must not be null.");
    this.key = new byte[length];
    System.arraycopy(key, offset, this.key, 0, length);
  }

}
