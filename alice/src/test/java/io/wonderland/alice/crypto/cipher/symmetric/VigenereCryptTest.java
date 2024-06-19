package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.VigenereKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class VigenereCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];

    StreamCipher cipher = new VigenereCrypt();

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

  }


  @Test
  void encryptDecrypt() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedtext = new byte[ciphertext.length];

    byte[] codeKeys = "1qazxsw23edu78ik,.l/';".getBytes();
    VigenereKey key = new VigenereKey(codeKeys);
    KeyParameter<VigenereKey> keyParameter = new KeyParameter<>(key);

    StreamCipher cipher = new VigenereCrypt();
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
    VigenereKey key1 = new VigenereKey(200, new byte[]{1, 2, 3, 4, 5});
    KeyParameter<VigenereKey> keyParameter1 = new KeyParameter<>(key1);
    StreamCipher cipher2 = new VigenereCrypt();
    cipher2.init(true, keyParameter1);

    log.info("PlainText '{}'", new String(plaintext));
    log.info("Key '{}'", keyParameter1.getKey());

    //Encryption test
    int processedBytes2 = cipher2.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes2);
    log.info("Encrypted Ciphertext '{}'", new String(ciphertext));
    assertThat(processedBytes2).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher2.init(false, keyParameter1);
    assertThat(cipher2.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedtext).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedtext));
  }

}
