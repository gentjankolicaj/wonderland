package io.wonderland.alice.crypto.key;

import io.wonderland.alice.crypto.Algorithms;
import javax.crypto.SecretKey;

@SuppressWarnings("all")
public class NullSecretKey implements SecretKey {

  @Override
  public String getAlgorithm() {
    return Algorithms.NULL.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.NULL.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return null;
  }
}
