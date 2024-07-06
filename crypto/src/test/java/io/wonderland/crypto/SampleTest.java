package io.wonderland.crypto;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.io.BaseEncoding;

@Slf4j
class SampleTest extends AbstractTest {


  @Test
  void sample() throws GeneralSecurityException {
    String input = "Hello world !!!`1234567890-=+_)(*&^%$#@!~{}|?><S:";
    log.info("Input : {}", input);

    //Symmetric key
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG",
        16);
    SymmetricCrypto symmetricCrypto = new SymmetricCrypto(CSP_NAME, "AES/CBC/CTSPadding", secretKey,
        ivParameterSpec);
    byte[] symmetricKeyEncrypted = symmetricCrypto.encrypt(input.getBytes());
    byte[] symmetricKeyDecrypted = symmetricCrypto.decrypt(symmetricKeyEncrypted);
    log.info("AES/CBC/CTSPadding encrypted : {}", new String(symmetricKeyEncrypted));
    log.info("AES/CBC/CTSPadding decrypted : {}", new String(symmetricKeyDecrypted));

    //Asymmetric key
    KeyPair keyPair = KeyPairUtils.generateKeyPair("RSA", 4096);
    AsymmetricCrypto asymmetricCrypto = new AsymmetricCrypto(CSP_NAME, "RSA", keyPair);
    byte[] asymmetricKeyEncrypted = asymmetricCrypto.encrypt(input.getBytes());
    byte[] asymmetricKeyDecrypted = asymmetricCrypto.decrypt(asymmetricKeyEncrypted);
    log.info("RSA encrypted : {}", new String(asymmetricKeyEncrypted));
    log.info("RSA decrypted : {}", new String(asymmetricKeyDecrypted));

  }


  @Test
  void test() {
    String hex = "6C73D5240A948C86981BC294814D";
    String message = "attack at dawn";
    for (byte b : message.getBytes(StandardCharsets.US_ASCII)) {
      System.out.print(" " + b);
    }

    System.out.println();
    byte[] plaintext = message.getBytes(StandardCharsets.US_ASCII);
    byte[] ciphertext = BaseEncoding.base16().decode(hex);
    int[] key = new int[ciphertext.length];
    for (int i = 0, len = ciphertext.length; i < len; i++) {
      key[i] = ciphertext[i] ^ plaintext[i];
    }

    String message1 = "attack at dusk";
    byte[] m1 = message1.getBytes(StandardCharsets.US_ASCII);
    byte[] c1 = new byte[m1.length];
    for (int i = 0, len = key.length; i < len; i++) {
      c1[i] = (byte) (key[i] ^ m1[i]);
    }
    String hex1 = BaseEncoding.base16().encode(c1);
    System.out.println(hex1);

  }


}
