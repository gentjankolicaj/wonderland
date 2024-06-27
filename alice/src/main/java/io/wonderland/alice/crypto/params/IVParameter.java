package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameter;
import lombok.Getter;

@Getter
public class IVParameter implements CipherParameter {

  private final byte[] iv;

  public IVParameter(byte[] iv) {
    this(iv, 0, iv.length);
  }

  public IVParameter(byte[] iv, int offset, int length) {
    this.iv = new byte[length];
    System.arraycopy(iv, offset, this.iv, 0, length);
  }
}
