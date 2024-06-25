package io.wonderland.alice.jca.cipher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.alice.crypto.key.keypair.AliceRSAPrivateKey;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPublicKey;
import io.wonderland.alice.jca.AliceProvider;
import io.wonderland.base.ByteArrayBuilder;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
class RSATest {

  private static final String CSP_NAME = "Alice";
  private static final String TRANSFORMATION = "RSA";

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

    //private key args
    BigInteger n = BigInteger.valueOf(89 * 97);
    BigInteger p = BigInteger.valueOf(89);
    BigInteger q = BigInteger.valueOf(97);
    BigInteger e = BigInteger.valueOf(1919);
    BigInteger d = BigInteger.valueOf(383);
    BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
    BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
    BigInteger qInv = q.modInverse(p);

    AliceRSAPrivateKey privateKey = new AliceRSAPrivateKey(n, p, q, e, d, dp, dq, qInv);

    //Encryption test
    //cipher encrypt init
    cipher.init(1, privateKey);

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
    AliceRSAPublicKey publicKey = new AliceRSAPublicKey(n, e);
    cipher.init(2, publicKey);

    assertThat(cipher.doFinal(ciphertext)).hasSameSizeAs(plaintext);
    assertThat(cipher.doFinal(ciphertext)).containsExactly(plaintext);
  }

}