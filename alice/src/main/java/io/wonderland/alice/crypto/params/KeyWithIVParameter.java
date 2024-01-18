package io.wonderland.alice.crypto.params;


import io.wonderland.alice.crypto.CipherParameters;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyWithIVParameter implements CipherParameters {

  private byte[] key;
  private byte[] iv;


  public KeyWithIVParameter() {
    this(new byte[0], new byte[0]);
  }

  public KeyWithIVParameter(byte[] key, byte[] iv) {
    Objects.requireNonNull(key, "Key must not be null.");
    Objects.requireNonNull(iv, "IV must not be null.");
    this.key = new byte[key.length];
    this.iv = new byte[iv.length];
    System.arraycopy(key, 0, this.key, 0, key.length);
    System.arraycopy(iv, 0, this.iv, 0, iv.length);
  }


}
