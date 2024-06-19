package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class CaesarCryptTest {

  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedText = new byte[ciphertext.length];

    RawKeyParameter rawKeyParameter = new RawKeyParameter(new byte[]{5});

    StreamCipher cipher = new CaesarCrypt();
    cipher.init(true, rawKeyParameter);

    log.info("PlainText '{}'", new String(plaintext));
    log.info("Key '{}'", new String(rawKeyParameter.getKey()));

    //Encryption test
    int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes);
    log.info("Encrypted Ciphertext '{}'", new String(ciphertext));
    assertThat(processedBytes).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(false, rawKeyParameter);
    assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedText, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedText).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedText));

    //key parameter test
    KeyParameter<CaesarKey> keyParameter = new KeyParameter<>(new CaesarKey(10));
    StreamCipher cipher2 = new CaesarCrypt();
    cipher2.init(true, keyParameter);

    log.info("PlainText '{}'", new String(plaintext));
    log.info("Key '{}'", keyParameter.getKey().getShift());

    //Encryption test
    int processedBytes2 = cipher2.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes2);
    log.info("Encrypted Ciphertext '{}'", new String(ciphertext));
    assertThat(processedBytes2).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher2.init(false, keyParameter);
    assertThat(cipher2.processBytes(ciphertext, 0, ciphertext.length, decryptedText, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedText).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedText));
  }

}
