package io.wonderland.alice.crypto.cipher.key;

import io.wonderland.alice.crypto.cipher.AlgNames;
import java.security.Key;

public class NullKey implements Key {

  @Override
  public String getAlgorithm() {
    return AlgNames.Null;
  }

  @Override
  public String getFormat() {
    return null;
  }

  @Override
  public byte[] getEncoded() {
    return new byte[0];
  }
}
