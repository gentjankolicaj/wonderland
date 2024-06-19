package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AffineCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    StreamCipher cipher = new AffineCrypt();

    assertThatThrownBy(() -> new AffineKey(2, 2, 4)).withFailMessage(
            "Affine key invalid.Argument a must be coprime to m (alphabet size).")
        .isInstanceOf(IllegalArgumentException.class);

    KeyParameter<Key> wrappedKeyParam1 = new KeyParameter<>(new AffineKey(2, 3, 5));
    cipher.init(true, wrappedKeyParam1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Plaintext can't be empty.").isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, wrappedKeyParam1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0)
    ).withFailMessage("Ciphertext can't be empty.").isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];

    KeyParameter<Key> wrappedKeyParam = new KeyParameter<>(new AffineKey(13, 3, 21));

    StreamCipher cipher = new AffineCrypt();
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
