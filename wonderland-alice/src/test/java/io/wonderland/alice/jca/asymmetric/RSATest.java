package io.wonderland.alice.jca.asymmetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.jca.ProviderTest;
import io.wonderland.base.ByteArrayBuilder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class RSATest extends ProviderTest {

  private static final String CSP_NAME = "Alice";
  private static final String TRANSFORMATION = "RSA";


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
    assertThat(cipher.getIV()).isNull();
    assertThatThrownBy(cipher::getBlockSize)
        .hasMessage("Can't determine block size because cipher keys are null.")
        .isInstanceOf(IllegalStateException.class);
    assertThat(cipher.getParameters()).isNull();

    KeyPair keyPair = getRSAKeys();

    //Encryption test
    //cipher encrypt init
    cipher.init(1, keyPair.getPrivate());

    byte[] hello = "Hello world".getBytes(StandardCharsets.UTF_8);
    byte[] numbers = "1,2,3,4,5,6,7,8,9,10".getBytes(StandardCharsets.UTF_8);
    byte[] lastMsg = "Last message".getBytes(StandardCharsets.UTF_8);

    //encryption process
    cipher.update(hello);
    cipher.update(numbers);
    byte[] ciphertext = cipher.doFinal(lastMsg);

    //get full plaintext
    byte[] plaintext = new ByteArrayBuilder().append(hello).append(numbers).append(lastMsg)
        .toByteArray();
    assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

    //Decryption test
    //re-initialize cipher for decryption
    cipher.init(2, keyPair.getPublic());

    assertThat(cipher.doFinal(ciphertext)).hasSameSizeAs(plaintext);
    assertThat(cipher.doFinal(ciphertext)).containsExactly(plaintext);
  }

  private KeyPair getRSAKeys() throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(512);
    return keyGen.genKeyPair();

  }

}