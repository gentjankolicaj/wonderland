package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.VernamKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class VernamCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];

    StreamCipher cipher = new VernamCrypt();

    RawKeyParameter rawKeyParameter0 = new RawKeyParameter(new byte[]{});
    cipher.init(true, rawKeyParameter0);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Key can't be null or empty.").isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() ->
        cipher.processByte((byte) 2)
    ).withFailMessage("Key can't be null or empty.").isInstanceOf(IllegalArgumentException.class);

    RawKeyParameter rawKeyParameter1 = new RawKeyParameter(new byte[]{5});
    cipher.init(true, rawKeyParameter1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Plaintext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, rawKeyParameter1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Ciphertext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    cipher.init(true, rawKeyParameter1);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Vernam cipher requires key length bigger/equal to plaintext length.")
        .isInstanceOf(IllegalArgumentException.class);

  }


  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World &&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];

    byte[] codeKeys = "1qazxsw23edc4rfv5tgb6yh=nm445ik,.l/';".getBytes();
    VernamKey key = new VernamKey(codeKeys);
    KeyParameter<VernamKey> keyParameter = new KeyParameter<>(key);

    StreamCipher cipher = new VernamCrypt();
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

    //key parameter test
    VernamKey key1 = new VernamKey(200,
        new byte[]{1, 2, 3, 4, 5, 12, 2, 3, 4, 4, 5, 5, 6, 6, 6, 7, 8, 9, 9, 1, 2, 3, 4, 5, 6, 67,
            2, 2, 3, 4, 6, 7, 7, 8, 99, 8, 76, 5, 54, 3, 45});
    KeyParameter<VernamKey> keyParameter1 = new KeyParameter<>(key1);
    StreamCipher cipher1 = new VernamCrypt();
    cipher1.init(true, keyParameter1);

    log.info("PlainText '{}'", new String(plaintext));
    log.info("Key '{}'", keyParameter1.getKey());

    //Encryption test
    int processedBytes2 = cipher1.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes2);
    log.info("Encrypted Ciphertext '{}'", new String(ciphertext));
    assertThat(processedBytes2).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher1.init(false, keyParameter1);
    assertThat(cipher1.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedtext).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedtext));
  }

}
