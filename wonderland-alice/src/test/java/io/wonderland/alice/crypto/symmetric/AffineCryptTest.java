package io.wonderland.alice.crypto.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AffineCryptTest {

  @Test
  void init() {
    StreamCipher cipher = new AffineCrypt();

    assertThatThrownBy(() -> new AffineKey(2, 2, 4))
        .hasMessage("Affine key invalid.Argument a must be coprime to m (alphabet size).")
        .isInstanceOf(IllegalArgumentException.class);

    KeyParameter<Key> keyParam = new KeyParameter<>(new OTPKey(12, 10));
    assertThatThrownBy(() -> cipher.init(true, keyParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    KeyWithIVParameter<Key> keyWithIVParam = new KeyWithIVParameter<>(
        new OTPKey(10, 10, 123, 1254, 12, 5), new byte[]{1, 2, 3, 4});
    assertThatThrownBy(() -> cipher.init(true, keyWithIVParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    byte[] rawKey = "12".getBytes(StandardCharsets.UTF_8);
    RawKeyParameter rawKeyParam = new RawKeyParameter(rawKey);
    assertThatThrownBy(() -> cipher.init(true, rawKeyParam))
        .hasMessage(cipher.invalidParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    ParameterList parameterList = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    assertThatThrownBy(() -> cipher.init(true, parameterList))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    //positive tests
    KeyParameter<Key> keyParam1 = new KeyParameter<>(new AffineKey(13, 3, 21));
    assertThatCode(() -> cipher.init(true, keyParam1)).doesNotThrowAnyException();

    KeyWithIVParameter<Key> keyWithIVParam1 = new KeyWithIVParameter<>(new AffineKey(13, 3, 21),
        new byte[]{1, 2, 3, 4});
    assertThatCode(() -> cipher.init(true, keyWithIVParam1)).doesNotThrowAnyException();

    ParameterList parameterList1 = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    assertThatCode(() -> cipher.init(true, parameterList1)).doesNotThrowAnyException();

  }


  @Test
  void encryptDecrypt() {
    byte[] plaintext0 = "".getBytes();
    byte[] ciphertext0 = new byte[plaintext0.length];

    StreamCipher cipher = new AffineCrypt();

    //negative tests
    KeyParameter<Key> keyParam = new KeyParameter<>(new AffineKey(2, 3, 5));
    cipher.init(true, keyParam);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext0.length, ciphertext0, 0))
        .hasMessage("Plaintext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, keyParam);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext0.length, ciphertext0, 0))
        .hasMessage("Ciphertext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];
    KeyParameter<Key> wrappedKeyParam = new KeyParameter<>(new AffineKey(13, 3, 21));
    cipher.init(true, wrappedKeyParam);

    log.info("Plaintext '{}'", new String(plaintext));
    log.info("Key '{}'", wrappedKeyParam.getKey());

    //Encryption test
    int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes);
    log.info("Encrypted Ciphertext {}", new String(ciphertext));
    assertThat(processedBytes).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(false, wrappedKeyParam);
    assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedtext).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedtext));
  }

}
