package io.wonderland.alice.crypto.key.secretkey;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.AffineKeyASN1Codec;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class AffineKeyTest {

  @Test
  void constructors() {
    AffineKey affineKey = new AffineKey(13, 3);
    assertThat(affineKey.getA()).isEqualTo(BigInteger.valueOf(13));
    assertThat(affineKey.getB()).isEqualTo(BigInteger.valueOf(3));
    assertThat(affineKey.getM()).isEqualTo(
        BigInteger.valueOf(CharsetsUtils.getDefaultAlphabetSize()));

    AffineKey affineKey1 = new AffineKey(BigInteger.valueOf(13), BigInteger.valueOf(3));
    assertThat(affineKey1.getA()).isEqualTo(BigInteger.valueOf(13));
    assertThat(affineKey1.getB()).isEqualTo(BigInteger.valueOf(3));
    assertThat(affineKey1.getM()).isEqualTo(
        BigInteger.valueOf(CharsetsUtils.getDefaultAlphabetSize()));

    AffineKey affineKey2 = new AffineKey(BigInteger.valueOf(13), BigInteger.valueOf(3),
        BigInteger.valueOf(21));
    assertThat(affineKey2.getA()).isEqualTo(BigInteger.valueOf(13));
    assertThat(affineKey2.getB()).isEqualTo(BigInteger.valueOf(3));
    assertThat(affineKey2.getM()).isEqualTo(BigInteger.valueOf(21));

  }

  @Test
  void getAlgorithm() {
    AffineKey affineKey = new AffineKey(13, 3);
    assertThat(affineKey.getAlgorithm()).isEqualTo(Algorithms.AFFINE.getName());
  }

  @Test
  void getFormat() {
    AffineKey affineKey = new AffineKey(13, 3);
    assertThat(affineKey.getFormat()).isEqualTo(Algorithms.AFFINE.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    AffineKey affineKey = new AffineKey(13, 3, 21);
    assertThat(affineKey.getEncoded()).isEqualTo(
        AffineKeyASN1Codec.getInstance().encoder().apply(affineKey));
  }
}