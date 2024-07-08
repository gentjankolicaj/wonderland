package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class SecretKeyUtilsTest extends AbstractTest {

  @Test
  void createGenerator() throws GeneralSecurityException {
    assertThat(SecretKeyUtils.createGenerator(CSP_NAME, "HmacSHA256")).isNotNull();
  }

  @Test
  void generateSecretKey() throws GeneralSecurityException {
    assertThat(SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256")).isNotNull();
    assertThat(SecretKeyUtils.generateSecretKey(CSP_NAME, "AES")).isNotNull();
  }

  @Test
  void generateSecretKey2() throws GeneralSecurityException {
    SecretKey hmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256", 256);
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES", 256);
    assertThat(hmacKey.getEncoded()).hasSize(256 / 8).isNotNull();
    assertThat(aesKey.getEncoded()).hasSize(256 / 8).isNotNull();
  }

  @Test
  void generateSecretKey3() throws GeneralSecurityException {
    SecretKey hmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256", 256,
        new SecureRandom());
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES", 256, new SecureRandom());
    assertThat(hmacKey.getEncoded()).hasSize(256 / 8).isNotNull();
    assertThat(aesKey.getEncoded()).hasSize(256 / 8).isNotNull();
  }

  @Test
  void createSecretKeySpec() {
    byte[] keyBytes = Hex.decode(
        "2ccd85dfc8d18cb5d84fef4b198554679fece6e8692c9147b0da983f5b7bd417");
    assertThat(SecretKeyUtils.createSecretKeySpec("HmacSHA256", keyBytes)).isNotNull();
  }


  @Test
  void generatePKCS5Scheme2() {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] salt = Hex.decode("bbaa99887766554433221100");
    assertThat(SecretKeyUtils.generatePKCS5Scheme2(input.toCharArray(), salt, 2, new SHA256Digest(),
        256)).isNotNull();

    //hasSize(32) because returns byte[] and 256/8=32 bytes length
    assertThat(SecretKeyUtils.generatePKCS5Scheme2(input.toCharArray(), salt, 2, new SHA256Digest(),
        256)).hasSize(32);
  }

  @Test
  void generateSCRYPT() {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] salt = Hex.decode("bbaa99887766554433221100");
    assertThat(SecretKeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 16)).isNotNull()
        .hasSize(16);
    assertThat(SecretKeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 32)).isNotNull()
        .hasSize(32);
    assertThat(SecretKeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 256)).isNotNull()
        .hasSize(256);
    log.debug("generateSCRYPT() : {}",
        new String(SecretKeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 64)));
  }

  @Disabled("To study key wrapping")
  @Test
  void keyWrapping() throws GeneralSecurityException {
    SecretKey aes = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA");

    byte[] wrappedKey = SecretKeyUtils.wrapKey(CSP_NAME, "AESKW", keyPair.getPublic(), aes);
    Key unwrappedKey = SecretKeyUtils.unwrapKey(CSP_NAME, "AESKW", keyPair.getPrivate(),
        wrappedKey, "AES", 1);
    assertThat(unwrappedKey.getEncoded()).containsExactly(aes.getEncoded());
  }

}