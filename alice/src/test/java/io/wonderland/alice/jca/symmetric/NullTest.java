package io.wonderland.alice.jca.symmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.alice.crypto.key.NullSecretKey;
import io.wonderland.alice.jca.ProviderTest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class NullTest extends ProviderTest {

  private static final String CSP_NAME = "Alice";
  private static final String TRANSFORMATION = "Null";


  @Test
  void cipher()
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    assertThatCode(() -> Cipher.getInstance(TRANSFORMATION, CSP_NAME)).doesNotThrowAnyException();
    assertThat(Cipher.getInstance(TRANSFORMATION, CSP_NAME).getAlgorithm())
        .isEqualTo(TRANSFORMATION);
  }

  @Test
  void cipherEncryptDecrypt()
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION, CSP_NAME);
    assertThat(cipher.getAlgorithm()).isEqualTo(TRANSFORMATION);
    assertThat(cipher.getIV()).isEmpty();
    assertThat(cipher.getBlockSize()).isEqualTo(-1);
    assertThat(cipher.getParameters()).isNull();

    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    NullSecretKey key = new NullSecretKey();

    //Encryption test
    //cipher encrypt init
    cipher.init(1, key);

    byte[] ciphertext = cipher.doFinal(plaintext);
    assertThat(ciphertext).hasSameSizeAs(plaintext)
        .isNotEmpty().containsExactly(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(2, key);

    assertThat(cipher.doFinal(ciphertext)).hasSameSizeAs(plaintext);
    assertThat(cipher.doFinal(ciphertext)).containsExactly(plaintext);
  }

}