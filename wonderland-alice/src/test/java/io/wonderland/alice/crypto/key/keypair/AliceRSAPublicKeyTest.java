package io.wonderland.alice.crypto.key.keypair;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.RSAKeyPairASN1Codec;
import java.math.BigInteger;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class AliceRSAPublicKeyTest {

  @Test
  void constructs() {
    AliceRSAPublicKey key = new AliceRSAPublicKey(1919, 89 * 97);
    assertThat(key).isNotNull();
    assertThat(key.getPublicExponent()).isNotNull();
    assertThat(key.getModulus()).isNotNull();

    AliceRSAPublicKey key1 = new AliceRSAPublicKey(BigInteger.valueOf(1919),
        BigInteger.valueOf(89 * 97));
    assertThat(key1).isNotNull();
    assertThat(key1.getPublicExponent()).isNotNull();
    assertThat(key1.getModulus()).isNotNull();
  }

  @Test
  void getAlgorithm() {
    AliceRSAPublicKey key = new AliceRSAPublicKey(1919, 89 * 97);
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.RSA.getName());

  }

  @Test
  void getFormat() {
    AliceRSAPublicKey key = new AliceRSAPublicKey(1919, 89 * 97);
    assertThat(key.getFormat()).isEqualTo(Algorithms.RSA.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    Function<AliceRSAPublicKey, byte[]> encoder = RSAKeyPairASN1Codec.getInstance().publicKeyCodec()
        .encoder();
    assertThat(new AliceRSAPublicKey(1919, 89 * 97).getEncoded()).containsExactly(
        encoder.apply(new AliceRSAPublicKey(1919, 89 * 97)));
  }

  @Test
  void getPublicExponent() {
    AliceRSAPublicKey key = new AliceRSAPublicKey(1919, 89 * 97);
    assertThat(key.getPublicExponent()).isEqualTo(BigInteger.valueOf(1919));

    AliceRSAPublicKey key1 = new AliceRSAPublicKey(BigInteger.valueOf(1919),
        BigInteger.valueOf(89 * 97));
    assertThat(key1).isNotNull();
    assertThat(key1.getPublicExponent()).isEqualTo(BigInteger.valueOf(1919));
  }

  @Test
  void getModulus() {
    AliceRSAPublicKey key = new AliceRSAPublicKey(1919, 89 * 97);
    assertThat(key.getModulus()).isEqualTo(BigInteger.valueOf(89 * 97));

    AliceRSAPublicKey key1 = new AliceRSAPublicKey(BigInteger.valueOf(1919),
        BigInteger.valueOf(89 * 97));
    assertThat(key1.getModulus()).isEqualTo(BigInteger.valueOf(89 * 97));
  }


}