package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class OTPCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];

    StreamCipher cipher = new OTPCrypt();

    RawKeyParameter rawKeyParameter0 = new RawKeyParameter(new byte[]{});
    assertThatThrownBy(() -> cipher.init(true, rawKeyParameter0))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0))
        .withFailMessage("Key can't be null or empty.")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> cipher.processByte((byte) 2))
        .withFailMessage("Key can't be null or empty.")
        .isInstanceOf(IllegalArgumentException.class);

    KeyParameter<OTPKey> keyParameter = new KeyParameter<>(
        new OTPKey(10, 1, 2, 2, 3, 2, 3, 4, 5, 5));
    cipher.init(true, keyParameter);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0))
        .withFailMessage("Plaintext can't be empty.")
        .isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, keyParameter);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0))
        .withFailMessage("Ciphertext can't be empty.")
        .isInstanceOf(IllegalArgumentException.class);

    cipher.init(true, keyParameter);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0))
        .withFailMessage("OTP cipher requires key length bigger/equal to plaintext length.")
        .isInstanceOf(IllegalArgumentException.class);

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
