package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.params.KeyParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class VernamCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    log.info("Using Vernam cipher ...");
    StreamCipher cipher = new VernamCrypt();

    KeyParameter keyParameter0 = new KeyParameter(new byte[]{});
    cipher.init(true, keyParameter0);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Key can't be null or empty.").isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() ->
        cipher.processByte((byte) 2)
    ).withFailMessage("Key can't be null or empty.").isInstanceOf(IllegalArgumentException.class);

    KeyParameter keyParameter1 = new KeyParameter(new byte[]{5});
    cipher.init(true, keyParameter1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Plaintext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, keyParameter1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Ciphertext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    cipher.init(true, keyParameter1);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Vernam cipher requires key length bigger/equal to plaintext length.")
        .isInstanceOf(IllegalArgumentException.class);

  }


  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];

    byte[] key = "1qazxsw23edc4rfv5tgb6yh=nm445ik,.l/';".getBytes();
    KeyParameter keyParameter = new KeyParameter(key);

    StreamCipher cipher = new VernamCrypt();
    cipher.init(true, keyParameter);

    log.info("Using Vernam cipher ...");
    log.info("Plaintext '{}'", new String(plaintext));
    log.info("Key '{}'", new String(keyParameter.getKey()));

    //Encryption test
    int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes);
    log.info("Encrypted Ciphertext {}", new String(ciphertext));
    assertThat(processedBytes).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(false, keyParameter);
    assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(ciphertext.length);
    assertThat(decryptedtext).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedtext));
  }

}
