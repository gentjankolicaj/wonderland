package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameters;
import lombok.Getter;

@Getter
public class IVWithParameter implements CipherParameters {

  private final byte[] iv;
  private final CipherParameters parameter;

  public IVWithParameter(CipherParameters parameter, byte[] iv) {
    this(parameter, iv, 0, iv.length);
  }

  public IVWithParameter(CipherParameters parameter, byte[] iv, int ivOff, int ivLen) {
    this.iv = new byte[ivLen];
    this.parameter = parameter;
    System.arraycopy(iv, ivOff, this.iv, 0, ivLen);
  }

}
