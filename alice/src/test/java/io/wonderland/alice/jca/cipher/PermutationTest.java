package io.wonderland.alice.jca.cipher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.alice.crypto.key.secretkey.PermutationKey;
import io.wonderland.alice.jca.AliceProvider;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled //todo: to implement permutation & disable test
@Slf4j
class PermutationTest {

  private static final String CSP_NAME = "Alice";
  private static final String TRANSFORMATION = "Permutation";

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

    byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
    PermutationKey key = new PermutationKey(0, 5, 3, 4, 2, 1);

    //Encryption test
    //cipher encrypt init
    cipher.init(1, key);

    byte[] ciphertext = cipher.doFinal(plaintext);
    assertThat(ciphertext).hasSameSizeAs(plaintext)
        .isNotEmpty()
        .isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(2, key);

    assertThat(cipher.doFinal(ciphertext)).hasSameSizeAs(plaintext);
    assertThat(cipher.doFinal(ciphertext)).containsExactly(plaintext);
  }


  void t() {

  }

}