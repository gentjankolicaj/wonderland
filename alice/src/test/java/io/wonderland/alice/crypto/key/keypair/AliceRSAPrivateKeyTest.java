package io.wonderland.alice.crypto.key.keypair;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.RSAKeyPairASN1Codec;
import java.math.BigInteger;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class AliceRSAPrivateKeyTest {

  @Test
  void constructors() {
    //383=1919^-1 % 8448
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key).isNotNull();
    assertThat(key.getPublicExponent()).isNotNull();
    assertThat(key.getPrivateExponent()).isNotNull();
    assertThat(key.getPrimeP()).isNotNull();
    assertThat(key.getPrimeQ()).isNotNull();
    assertThat(key.getPrimeExponentP()).isNotNull();
    assertThat(key.getPrimeExponentQ()).isNotNull();
    assertThat(key.getCrtCoefficient()).isNotNull();
    assertThat(key.getModulus()).isNotNull();

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1).isNotNull();
    assertThat(key1.getPublicExponent()).isNotNull();
    assertThat(key1.getPrivateExponent()).isNotNull();
    assertThat(key1.getPrimeP()).isNotNull();
    assertThat(key1.getPrimeQ()).isNotNull();
    assertThat(key1.getPrimeExponentP()).isNotNull();
    assertThat(key1.getPrimeExponentQ()).isNotNull();
    assertThat(key1.getCrtCoefficient()).isNotNull();
    assertThat(key1.getModulus()).isNotNull();

    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2).isNotNull();
    assertThat(key2.getPublicExponent()).isNotNull();
    assertThat(key2.getPrivateExponent()).isNotNull();
    assertThat(key2.getPrimeP()).isNotNull();
    assertThat(key2.getPrimeQ()).isNotNull();
    assertThat(key2.getPrimeExponentP()).isNotNull();
    assertThat(key2.getPrimeExponentQ()).isNotNull();
    assertThat(key2.getCrtCoefficient()).isNotNull();
    assertThat(key2.getModulus()).isNotNull();

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3).isNotNull();
    assertThat(key3.getPublicExponent()).isNotNull();
    assertThat(key3.getPrivateExponent()).isNotNull();
    assertThat(key3.getPrimeP()).isNotNull();
    assertThat(key3.getPrimeQ()).isNotNull();
    assertThat(key3.getPrimeExponentP()).isNotNull();
    assertThat(key3.getPrimeExponentQ()).isNotNull();
    assertThat(key3.getCrtCoefficient()).isNotNull();
    assertThat(key3.getModulus()).isNotNull();

    //
    BigInteger n = BigInteger.valueOf(89 * 97);
    BigInteger p = BigInteger.valueOf(89);
    BigInteger q = BigInteger.valueOf(97);
    BigInteger e = BigInteger.valueOf(1919);
    BigInteger d = BigInteger.valueOf(383);
    BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
    BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
    BigInteger qInv = q.modInverse(p);

    AliceRSAPrivateKey key4 = new AliceRSAPrivateKey(n, p, q, e, d, dp, dq, qInv);
    assertThat(key4).isNotNull();
    assertThat(key4.getPublicExponent()).isNotNull();
    assertThat(key4.getPrivateExponent()).isNotNull();
    assertThat(key4.getPrimeP()).isNotNull();
    assertThat(key4.getPrimeQ()).isNotNull();
    assertThat(key4.getPrimeExponentP()).isNotNull();
    assertThat(key4.getPrimeExponentQ()).isNotNull();
    assertThat(key4.getCrtCoefficient()).isNotNull();
    assertThat(key4.getModulus()).isNotNull();
  }

  @Test
  void getAlgorithm() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.RSA.getName());
  }

  @Test
  void getFormat() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key.getFormat()).isEqualTo(Algorithms.RSA.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    Function<AliceRSAPrivateKey, byte[]> encoder = RSAKeyPairASN1Codec.getInstance()
        .privateKeyCodec().encoder();
    assertThat(new AliceRSAPrivateKey(89, 97, 1919).getEncoded()).containsExactly(
        encoder.apply(new AliceRSAPrivateKey(89, 97, 1919)));
  }

  @Test
  void getPublicExponent() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key).isNotNull();
    assertThat(key.getPublicExponent()).isEqualTo(BigInteger.valueOf(1919));

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key1).isNotNull();
    assertThat(key1.getPublicExponent()).isEqualTo(BigInteger.valueOf(1919));
  }

  @Test
  void getPrimeP() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getPrimeP()).isNotNull().isEqualTo(BigInteger.valueOf(89));

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getPrimeP()).isNotNull().isEqualTo(BigInteger.valueOf(89));

    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getPrimeP()).isNotNull().isEqualTo(BigInteger.valueOf(89));

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getPrimeP()).isNotNull().isEqualTo(BigInteger.valueOf(89));
  }

  @Test
  void getPrimeQ() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getPrimeQ()).isNotNull().isEqualTo(BigInteger.valueOf(97));

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getPrimeQ()).isNotNull().isEqualTo(BigInteger.valueOf(97));

    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getPrimeQ()).isNotNull().isEqualTo(BigInteger.valueOf(97));

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getPrimeQ()).isNotNull().isEqualTo(BigInteger.valueOf(97));
  }

  @Test
  void getPrimeExponentP() {
    // 1919 * 383 mod (89-1 * 97-1) ≡ 1 (mod 8448)
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getPrimeExponentP()).isNotNull();

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getPrimeExponentP()).isNotNull();

    BigInteger dp = BigInteger.valueOf(383).mod(BigInteger.valueOf(89 - 1));
    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getPrimeExponentP()).isNotNull().isEqualTo(dp);

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getPrimeExponentP()).isNotNull().isEqualTo(dp);
  }

  @Test
  void getPrimeExponentQ() {
    // 1919 * 383 mod (89-1 * 97-1) ≡ 1 (mod 8448)
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getPrimeExponentQ()).isNotNull();

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getPrimeExponentQ()).isNotNull();

    BigInteger dq = BigInteger.valueOf(383).mod(BigInteger.valueOf(97 - 1));
    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getPrimeExponentQ()).isNotNull().isEqualTo(dq);

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getPrimeExponentQ()).isNotNull().isEqualTo(dq);
  }

  @Test
  void getCrtCoefficient() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getCrtCoefficient()).isNotNull();

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getCrtCoefficient()).isNotNull();

    BigInteger qInv = BigInteger.valueOf(97).modInverse(BigInteger.valueOf(89));
    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getCrtCoefficient()).isNotNull().isEqualTo(qInv);

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getCrtCoefficient()).isNotNull().isEqualTo(qInv);
  }

  @Test
  void getPrivateExponent() {
    // 1919 * 383 mod (89-1 * 97-1) ≡ 1 (mod 8448)
    BigInteger d = BigInteger.valueOf(383);
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getPrivateExponent()).isNotNull();

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getPrivateExponent()).isNotNull();

    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getPrivateExponent()).isNotNull().isEqualTo(d);

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getPrivateExponent()).isNotNull().isEqualTo(d);
  }

  @Test
  void getModulus() {
    AliceRSAPrivateKey key = new AliceRSAPrivateKey(89, 97);
    assertThat(key.getModulus()).isEqualTo(BigInteger.valueOf(8633));

    AliceRSAPrivateKey key1 = new AliceRSAPrivateKey(BigInteger.valueOf(89),
        BigInteger.valueOf(97));
    assertThat(key1.getModulus()).isEqualTo(BigInteger.valueOf(8633));

    AliceRSAPrivateKey key2 = new AliceRSAPrivateKey(89, 97, 1919);
    assertThat(key2.getModulus()).isEqualTo(BigInteger.valueOf(8633));

    AliceRSAPrivateKey key3 = new AliceRSAPrivateKey(BigInteger.valueOf(89), BigInteger.valueOf(97),
        BigInteger.valueOf(1919));
    assertThat(key3.getModulus()).isEqualTo(BigInteger.valueOf(8633));
  }

}