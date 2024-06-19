package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.AffineKeyASN1Codec;
import java.math.BigInteger;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;

/**
 * Affine cipher key definition class.
 * <pre>
 * --ASN1 definition at path : /resources/asn1/affine.asn
 * </pre>
 */
@Getter
@ToString
public final class AffineKey implements SecretKey {

  private static final Function<AffineKey, byte[]> encoder = AffineKeyASN1Codec.getInstance()
      .encoder();

  private final BigInteger a;
  private final BigInteger b;
  private final BigInteger m;

  public AffineKey(BigInteger a, BigInteger b) {
    this.a = a;
    this.b = b;
    this.m = BigInteger.valueOf(CharsetsUtils.getDefaultAlphabetSize());
    if (this.a.gcd(this.m).intValue() != 1) {
      throw new IllegalArgumentException(
          "Affine key invalid.Argument a must be coprime to m (alphabet size).");
    }
  }

  public AffineKey(BigInteger a, BigInteger b, BigInteger m) {
    this.a = a;
    this.b = b;
    this.m = m;
    if (this.a.gcd(this.m).intValue() != 1) {
      throw new IllegalArgumentException(
          "Affine key invalid.Argument a must be coprime to m (alphabet size).");
    }
  }

  public AffineKey(long a, long b, long m) {
    this.a = BigInteger.valueOf(a);
    this.b = BigInteger.valueOf(b);
    this.m = BigInteger.valueOf(m);
    if (this.a.gcd(this.m).intValue() != 1) {
      throw new IllegalArgumentException(
          "Affine key invalid.Argument a must be coprime to m (alphabet size).");
    }
  }

  public AffineKey(long a, long b) {
    this.m = BigInteger.valueOf(CharsetsUtils.getDefaultAlphabetSize());
    this.a = BigInteger.valueOf(a);
    this.b = BigInteger.valueOf(b);
    if (this.a.gcd(this.m).intValue() != 1) {
      throw new IllegalArgumentException(
          "Affine key invalid.Argument a must be coprime to m (alphabet size).");
    }
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.AFFINE.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.AFFINE.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return encoder.apply(this);
  }

}
