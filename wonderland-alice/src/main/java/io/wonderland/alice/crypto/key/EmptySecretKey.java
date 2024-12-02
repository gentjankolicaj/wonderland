package io.wonderland.alice.crypto.key;

import io.wonderland.alice.crypto.Algorithms;
import javax.crypto.SecretKey;


public class EmptySecretKey implements SecretKey {

  @Override
  public String getAlgorithm() {
    return Algorithms.EMPTY.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.EMPTY.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return new byte[0];
  }
}
