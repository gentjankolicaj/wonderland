package io.wonderland.alice.jca.cipher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.alice.crypto.cipher.key.ByteArrayKey;
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
import org.junit.jupiter.api.Test;

@Slf4j
class VernamTest {

  private static final String CSP_NAME = "Alice";
  private static final String TRANSFORMATION = "Vernam";

  static {
    Security.addProvider(new AliceProvider());
  }

  @Test
  void testCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    assertThatCode(() -> Cipher.getInstance(TRANSFORMATION, CSP_NAME)).doesNotThrowAnyException();
    assertThat(Cipher.getInstance(TRANSFORMATION, CSP_NAME).getAlgorithm()).isEqualTo(TRANSFORMATION);
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
    byte[] key = "1qazxsw23edc4rfv5tgb6yhnmju78ik,.l/';".getBytes();
    ByteArrayKey byteArrayKey = new ByteArrayKey(key);

    //Encryption test
    //cipher encrypt init
    cipher.init(1, byteArrayKey);

    byte[] ciphertext = cipher.doFinal(plaintext);
    assertThat(ciphertext).hasSameSizeAs(plaintext)
        .isNotEmpty()
        .isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(2, byteArrayKey);

    assertThat(cipher.doFinal(ciphertext)).hasSameSizeAs(plaintext);
    assertThat(cipher.doFinal(ciphertext)).containsExactly(plaintext);
  }

}