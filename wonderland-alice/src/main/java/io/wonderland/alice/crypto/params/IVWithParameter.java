package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameter;
import lombok.Getter;

@Getter
public class IVWithParameter implements CipherParameter {

  private final byte[] iv;
  private final CipherParameter parameter;

  public IVWithParameter(CipherParameter parameter, byte[] iv) {
    this(parameter, iv, 0, iv.length);
  }

  public IVWithParameter(CipherParameter parameter, byte[] iv, int ivOff, int ivLen) {
    this.iv = new byte[ivLen];
    this.parameter = parameter;
    System.arraycopy(iv, ivOff, this.iv, 0, ivLen);
  }

}
