package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameter;
import java.security.Key;
import java.util.Objects;
import lombok.Getter;

@Getter
public class KeyWithIVParameter<T extends Key> implements CipherParameter {

  private final T key;
  private final byte[] iv;

  public KeyWithIVParameter(T key, byte[] iv) {
    Objects.requireNonNull(key, "Key must not be null.");
    Objects.requireNonNull(iv, "IV must not be null.");
    this.key = key;
    this.iv = new byte[iv.length];
    System.arraycopy(iv, 0, this.iv, 0, iv.length);
  }

}
