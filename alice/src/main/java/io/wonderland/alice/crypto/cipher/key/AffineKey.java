package io.wonderland.alice.crypto.cipher.key;

import io.jmathematics.algorithm.EuclideanAlgorithm;
import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.cipher.AlgNames;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class AffineKey implements SecretKey {

  private final int a;
  private final int b;
  private final int m;

  public AffineKey(int a, int b, int m) {
    if (EuclideanAlgorithm.gcd(a, m) != 1) {
      throw new IllegalArgumentException("Affine key invalid.Argument a must be coprime to m (alphabet size).");
    }
    this.a = a;
    this.b = b;
    this.m = m;
  }

  public AffineKey(int a, int b) {
    this.m = CharsetsUtils.getDefaultAlphabetSize();
    if (EuclideanAlgorithm.gcd(a, m) != 1) {
      throw new IllegalArgumentException("Affine key invalid.Argument a must be coprime to m (alphabet size).");
    }
    this.a = a;
    this.b = b;
  }


  @Override
  public String getAlgorithm() {
    return AlgNames.Affine;
  }

  @Override
  public String getFormat() {
    return KeyFormats.INT.name();
  }

  @Override
  public byte[] getEncoded() {
    throw new UnsupportedOperationException("Todo: to implement util methods to encode int info into byte array.");
  }


}
