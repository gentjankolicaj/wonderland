package io.wonderland.alice.jca.cipher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.alice.crypto.key.secretkey.MonoalphabetKey;
import io.wonderland.alice.jca.AliceProvider;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MonoalphabetTest {

  private static final String CSP_NAME = "Alice";
  private static final String TRANSFORMATION = "Monoalphabet";

  static {
    Security.addProvider(new AliceProvider());
  }

  @Test
  void testCipher()
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    assertThatCode(() -> Cipher.getInstance(TRANSFORMATION, CSP_NAME)).doesNotThrowAnyException();
    assertThat(Cipher.getInstance(TRANSFORMATION, CSP_NAME).getAlgorithm()).isEqualTo(
        TRANSFORMATION);
  }

  @Test
  void testCipherEncryptDecrypt()
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION, CSP_NAME);
    assertThat(cipher.getAlgorithm()).isEqualTo(TRANSFORMATION);
    assertThat(cipher.getIV()).isEmpty();
    assertThat(cipher.getBlockSize()).isEqualTo(-1);
    assertThat(cipher.getParameters()).isNull();

    byte[] plaintext = "HA123B".getBytes();
    //1 -> code-point 49
    //2 -> code-point 50
    //3 -> code-point 51
    //A -> code-point 65 etc
    Map<Integer, Integer> keyMap = Map.of(0, 99, 49, 11, 50, 22, 51, 44, 65, 66, 66, 68, 72, 70);
    MonoalphabetKey monoalphabetKey = new MonoalphabetKey(keyMap);

    //Encryption test
    //cipher encrypt init
    cipher.init(1, monoalphabetKey);

    byte[] ciphertext = cipher.doFinal(plaintext);
    assertThat(ciphertext).hasSameSizeAs(plaintext)
        .isNotEmpty()
        .isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(2, monoalphabetKey);

    assertThat(cipher.doFinal(ciphertext)).hasSameSizeAs(plaintext);
    assertThat(cipher.doFinal(ciphertext)).containsExactly(plaintext);
  }

}