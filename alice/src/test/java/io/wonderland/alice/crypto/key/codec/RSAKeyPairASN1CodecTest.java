package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.codec.PrivateKeyCodec;
import io.wonderland.alice.codec.PublicKeyCodec;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPrivateKey;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPublicKey;
import java.math.BigInteger;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class RSAKeyPairASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(RSAKeyPairASN1Codec.getInstance()).isNotNull();
  }

  @Test
  void publicKeyCodec() {
    PublicKeyCodec<AliceRSAPublicKey> publicKeyCodec = RSAKeyPairASN1Codec.getInstance()
        .publicKeyCodec();

    Function<AliceRSAPublicKey, byte[]> encoder = publicKeyCodec.encoder();
    Function<byte[], AliceRSAPublicKey> decoder = publicKeyCodec.decoder();

    BigInteger n = BigInteger.valueOf(89 * 97);
    BigInteger e = BigInteger.valueOf(1919);

    AliceRSAPublicKey initialKey = new AliceRSAPublicKey(e, n);
    byte[] encoded = encoder.apply(initialKey);
    AliceRSAPublicKey decodedKey = decoder.apply(encoded);

    assertThat(decodedKey).isNotNull();
    assertThat(decodedKey.getPublicExponent()).isEqualTo(initialKey.getPublicExponent());
    assertThat(decodedKey.getModulus()).isEqualTo(initialKey.getModulus());
  }

  @Test
  void privateKeyCodec() {
    PrivateKeyCodec<AliceRSAPrivateKey> privateKeyCodec = RSAKeyPairASN1Codec.getInstance()
        .privateKeyCodec();

    Function<AliceRSAPrivateKey, byte[]> encoder = privateKeyCodec.encoder();
    Function<byte[], AliceRSAPrivateKey> decoder = privateKeyCodec.decoder();

    BigInteger n = BigInteger.valueOf(89 * 97);
    BigInteger p = BigInteger.valueOf(89);
    BigInteger q = BigInteger.valueOf(97);
    BigInteger e = BigInteger.valueOf(1919);
    BigInteger d = BigInteger.valueOf(383);
    BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
    BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
    BigInteger qInv = q.modInverse(p);

    AliceRSAPrivateKey initialKey = new AliceRSAPrivateKey(n, p, q, e, d, dp, dq, qInv);
    byte[] encoded = encoder.apply(initialKey);
    AliceRSAPrivateKey decodedKey = decoder.apply(encoded);

    assertThat(decodedKey).isNotNull();
    assertThat(decodedKey.getPublicExponent()).isEqualTo(initialKey.getPublicExponent());
    assertThat(decodedKey.getPrivateExponent()).isEqualTo(initialKey.getPrivateExponent());
    assertThat(decodedKey.getPrimeP()).isEqualTo(initialKey.getPrimeP());
    assertThat(decodedKey.getPrimeQ()).isEqualTo(initialKey.getPrimeQ());
    assertThat(decodedKey.getPrimeExponentP()).isEqualTo(initialKey.getPrimeExponentP());
    assertThat(decodedKey.getPrimeExponentQ()).isEqualTo(initialKey.getPrimeExponentQ());
    assertThat(decodedKey.getCrtCoefficient()).isEqualTo(initialKey.getCrtCoefficient());
    assertThat(decodedKey.getModulus()).isEqualTo(initialKey.getModulus());
  }
}