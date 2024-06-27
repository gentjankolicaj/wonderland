package io.wonderland.alice.crypto.cipher.asymmetric.rsa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.AsymmetricCipher;
import io.wonderland.alice.crypto.asymmetric.rsa.RSACrypt;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPrivateKey;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPublicKey;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.junit.jupiter.api.Test;

class RSACryptTest {


  @Test
  void constructor() {
    RSACrypt crypt = new RSACrypt();
    assertThat(crypt).isNotNull();
  }


  @Test
  void init() {
    AsymmetricCipher cipher = new RSACrypt();

    KeyParameter<Key> keyParam = new KeyParameter<>(new CaesarKey(12));
    assertThatThrownBy(() -> cipher.init(true, keyParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    //raw & iv to be used for negative key param tests
    byte[] iv = "hello world iv 132saf23235t6536".getBytes(StandardCharsets.UTF_8);
    byte[] rawKey = "la lala as23r343234(*&^%$#@!#$%&*()_".getBytes(StandardCharsets.UTF_8);

    KeyWithIVParameter<Key> keyWithIVParam = new KeyWithIVParameter<>(
        new OTPKey(10, 10, 123, 1254, 12, 5), iv);
    assertThatThrownBy(() -> cipher.init(true, keyWithIVParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    RawKeyParameter rawKeyParam = new RawKeyParameter(rawKey);
    assertThatThrownBy(() -> cipher.init(true, rawKeyParam))
        .hasMessage(cipher.invalidParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    ParameterList parameterList = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    parameterList.add(rawKeyParam);
    parameterList.add(keyWithIVParam);
    assertThatThrownBy(() -> cipher.init(true, parameterList))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    //positive tests
    KeyParameter<Key> keyParam1 = new KeyParameter<>(new AliceRSAPublicKey(1919, 8633));
    assertThatCode(() -> cipher.init(true, keyParam1)).doesNotThrowAnyException();

    KeyWithIVParameter<Key> keyWithIVParam1 = new KeyWithIVParameter<>(
        new AliceRSAPublicKey(1919, 8633), iv);
    assertThatCode(() -> cipher.init(true, keyWithIVParam1)).doesNotThrowAnyException();

    ParameterList parameterList1 = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    assertThatCode(() -> cipher.init(true, parameterList1)).doesNotThrowAnyException();

  }

  @Test
  void encryptDecrypt() {
    BigInteger n = BigInteger.valueOf(89 * 97);
    BigInteger p = BigInteger.valueOf(89);
    BigInteger q = BigInteger.valueOf(97);
    BigInteger e = BigInteger.valueOf(1919);
    BigInteger d = BigInteger.valueOf(383);
    BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
    BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
    BigInteger qInv = q.modInverse(p);

    AsymmetricCipher cipher = new RSACrypt();

    KeyParameter<AliceRSAPublicKey> publicKeyParam = new KeyParameter<>(
        new AliceRSAPublicKey(e, n));
    KeyParameter<AliceRSAPrivateKey> privateKeyParam = new KeyParameter<>(
        new AliceRSAPrivateKey(n, p, q, e, d, dp, dq, qInv));

    //init encrypt cipher
    cipher.init(true, publicKeyParam);

    //init decrypt cipher
    cipher.init(false, privateKeyParam);
  }

  @Test
  void getInputBlockSize() {
  }

  @Test
  void getOutputBlockSize() {
  }

  @Test
  void getAlgorithmName() {
    AsymmetricCipher cipher = new RSACrypt();
    assertThat(cipher.getAlgorithmName()).isEqualTo(Algorithms.RSA.getName());
  }

  @Test
  void processBlock() {
  }

  @Test
  void update() {
  }

  @Test
  void reset() {
  }

}