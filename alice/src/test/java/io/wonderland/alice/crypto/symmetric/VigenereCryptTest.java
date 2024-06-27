package io.wonderland.alice.crypto.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import io.wonderland.alice.crypto.key.secretkey.VigenereKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class VigenereCryptTest {

  @Test
  void negativeTest() {
    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    byte[] ciphertext = new byte[plaintext.length];

    StreamCipher cipher = new VigenereCrypt();

    RawKeyParameter rawKeyParam = new RawKeyParameter(new byte[]{});
    cipher.init(true, rawKeyParam);
    assertThatThrownBy(() ->
        cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0))
        .hasMessage("Key can't be null or empty.")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() ->
        cipher.processByte((byte) 2))
        .hasMessage("Key can't be null or empty.")
        .isInstanceOf(IllegalArgumentException.class);

    RawKeyParameter rawKeyParam1 = new RawKeyParameter(new byte[]{5});
    cipher.init(true, rawKeyParam1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0))
        .hasMessage("Plaintext can't be empty.")
        .isInstanceOf(IllegalArgumentException.class);

    cipher.init(false, rawKeyParam1);
    assertThatThrownBy(() ->
        cipher.processBytes(null, 0, plaintext.length, ciphertext, 0))
        .hasMessage("Ciphertext can't be empty.")
        .isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  void init() {
    StreamCipher cipher = new VigenereCrypt();

    KeyParameter<Key> keyParam = new KeyParameter<>(new OTPKey(12, 10));
    assertThatThrownBy(() -> cipher.init(true, keyParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    KeyWithIVParameter<Key> keyWithIVParam = new KeyWithIVParameter<>(
        new OTPKey(10, 10, 123, 1254, 12, 5), new byte[]{1, 2, 3, 4});
    assertThatThrownBy(() -> cipher.init(true, keyWithIVParam))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    ParameterList parameterList = new ParameterList();
    parameterList.add(keyParam);
    parameterList.add(keyWithIVParam);
    assertThatThrownBy(() -> cipher.init(true, parameterList))
        .hasMessage(cipher.invalidKeyTypeParamMessage())
        .isInstanceOf(IllegalArgumentException.class);

    //positive tests
    KeyParameter<Key> keyParam1 = new KeyParameter<>(new VigenereKey(100, new byte[]{1, 10, 2, 6}));
    assertThatCode(() -> cipher.init(true, keyParam1)).doesNotThrowAnyException();

    KeyWithIVParameter<Key> keyWithIVParam1 = new KeyWithIVParameter<>(
        new VigenereKey(100, new byte[]{1, 10, 2, 6}), new byte[]{1, 2, 3, 4});
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
