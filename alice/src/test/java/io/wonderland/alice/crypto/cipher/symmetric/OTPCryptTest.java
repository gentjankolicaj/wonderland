package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
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
class OTPCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];

    StreamCipher cipher = new OTPCrypt();

    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0))
        .hasMessage("Key can't be null or empty.")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> cipher.processByte((byte) 2))
        .hasMessage("Key can't be null or empty.")
        .isInstanceOf(IllegalArgumentException.class);

    KeyParameter<OTPKey> keyParam = new KeyParameter<>(new OTPKey(10, 1, 2, 2, 3, 2, 3, 4, 5, 5));
    cipher.init(true, keyParam);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0))
        .hasMessage("Plaintext can't be empty.")
        .isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, keyParam);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0))
        .hasMessage("Ciphertext can't be empty.")
        .isInstanceOf(IllegalArgumentException.class);

    cipher.init(true, keyParam);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0))
        .hasMessage("OTP cipher requires key length bigger/equal to plaintext length.")
        .isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  void init() {
    StreamCipher cipher = new OTPCrypt();

    KeyParameter<Key> keyParam = new KeyParameter<>(new CaesarKey(12));
    assertThatThrownBy(() -> cipher.init(true, keyParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    KeyWithIVParameter<Key> keyWithIVParam = new KeyWithIVParameter<>(new CaesarKey(10),
        new byte[]{1, 2, 3, 4});
    assertThatThrownBy(() -> cipher.init(true, keyWithIVParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    ParameterList parameterList = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    assertThatThrownBy(() -> cipher.init(true, parameterList))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    byte[] rawKey = "12".getBytes(StandardCharsets.UTF_8);
    RawKeyParameter rawKeyParam = new RawKeyParameter(rawKey);
    assertThatThrownBy(() -> cipher.init(true, rawKeyParam))
        .hasMessage(cipher.invalidParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    //positive tests
    KeyParameter<Key> keyParam1 = new KeyParameter<>(new OTPKey(256, 1, 1, 2, 2, 3, 4, 4));
    assertThatCode(() -> cipher.init(true, keyParam1)).doesNotThrowAnyException();

    KeyWithIVParameter<Key> keyWithIVParam1 = new KeyWithIVParameter<>(
        new OTPKey(256, 1, 1, 2, 2, 3, 4, 4), new byte[]{1, 2, 3, 4});
    assertThatCode(() -> cipher.init(true, keyWithIVParam1)).doesNotThrowAnyException();

    ParameterList parameterList1 = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    assertThatCode(() -> cipher.init(true, parameterList1)).doesNotThrowAnyException();
  }


  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];

    int[] keys = new int[]{1, 2, 2, 3, 4, 4, 5, 5, 6, 62, 3, 3, 4, 5, 6, 7, 9, 9, 1, 2, 3, 3, 4, 34,
        4, 2, 3, 3, 3, 3, 3, 2, 1, 11, 1, 2, 43, 2};
    OTPKey otpKey = new OTPKey(CharsetsUtils.getDefaultAlphabetSize(), keys);
    KeyParameter<OTPKey> keyParameter = new KeyParameter<>(otpKey);

    StreamCipher cipher = new OTPCrypt();
    cipher.init(true, keyParameter);

    log.info("Plaintext '{}'", new String(plaintext));
    log.info("Key '{}'", keyParameter.getKey());

    //Encryption test
    int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes);
    log.info("Encrypted Ciphertext '{}'", new String(ciphertext));
    assertThat(processedBytes).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(false, keyParameter);
    assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedtext).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedtext));
  }

}
