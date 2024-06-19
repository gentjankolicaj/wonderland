package io.wonderland.alice.crypto.cipher.symmetric;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.MonoalphabetKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import java.security.Key;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MonoalphabetCryptTest {


  @Test
  void encryptDecrypt() {
    byte[] plaintext = "HA123B".getBytes();
    byte[] ciphertext = new byte[plaintext.length];
    byte[] decryptedText = new byte[ciphertext.length];

    //1 -> code-point 49
    //2 -> code-point 50
    //3 -> code-point 51
    //A -> code-point 65 etc
    Map<Integer, Integer> keyMap = Map.of(0, 99, 49, 11, 50, 22, 51, 44, 65, 66, 66, 68, 72, 70);
    KeyParameter<Key> keyParameter = new KeyParameter<>(new MonoalphabetKey(keyMap));

    StreamCipher cipher = new MonoalphabetCrypt();
    cipher.init(true, keyParameter);

    log.info("Plaintext '{}'", new String(plaintext));
    log.info("Key '{}'", keyParameter.getKey());

    //Encryption test
    int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
    log.info("Processed bytes '{}' ", processedBytes);
    log.info("Encrypted Ciphertext {}", new String(ciphertext));
    assertThat(processedBytes).isEqualTo(plaintext.length);
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(false, keyParameter);
    assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedText, 0)).isEqualTo(
        ciphertext.length);
    assertThat(decryptedText).containsExactly(plaintext);
    log.info("Decrypted ciphertext '{}'", new String(decryptedText));
  }

}
