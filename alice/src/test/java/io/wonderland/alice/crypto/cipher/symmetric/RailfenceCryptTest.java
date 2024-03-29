package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.cipher.key.RailfenceKey;
import io.wonderland.alice.crypto.params.WrappedKeyParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class RailfenceCryptTest {

  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];

    WrappedKeyParameter<RailfenceKey> wrappedKeyParameter = new WrappedKeyParameter<>(new RailfenceKey(3));

    StreamCipher cipher = new RailfenceCrypt();
    cipher.init(true, wrappedKeyParameter);

    log.info("Using Railfence cipher ...");
    log.info("PlainText '{}'", new String(plaintext));
    log.info("Key '{}'", wrappedKeyParameter.getWrappedKey());

    //Encryption test
    int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes);
    log.info("Encrypted Ciphertext '{}'", new String(ciphertext));
    assertThat(processedBytes).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(false, wrappedKeyParameter);
    assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(ciphertext.length);
    assertThat(decryptedtext).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedtext));

  }
}
