package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SecretKeyHelperTest extends AbstractTest {


  @Test
  void constructors() throws NoSuchAlgorithmException, NoSuchProviderException {
    String algorithm = "AES";
    SecretKeyHelper secretKeyHelper = new SecretKeyHelper(algorithm);
    assertThat(secretKeyHelper.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(secretKeyHelper.getAlgorithm()).isEqualTo(algorithm);
    assertThat(secretKeyHelper.getKeyGenerator()).isNotNull();

    SecretKeyHelper secretKeyHelper1 = new SecretKeyHelper(CSP.SunJCE, algorithm);
    assertThat(secretKeyHelper1.getProvider()).isEqualTo(CSP.SunJCE);
    assertThat(secretKeyHelper1.getAlgorithm()).isEqualTo(algorithm);
    assertThat(secretKeyHelper1.getKeyGenerator()).isNotNull();
  }

  @Test
  void createGenerator() throws GeneralSecurityException {
    String algorithm = "HmacSHA256";
    SecretKeyHelper secretKeyHelper = new SecretKeyHelper(algorithm);
    assertThat(secretKeyHelper.createGenerator()).isNotNull();
  }

  @Test
  void generateSecretKey() throws GeneralSecurityException {
    String algorithm = "HmacSHA256";
    SecretKeyHelper secretKeyHelper = new SecretKeyHelper(algorithm);
    assertThat(secretKeyHelper.generateSecretKey()).isNotNull();
  }

  @Test
  void generateSecretKey2() throws GeneralSecurityException {
    String algorithm = "HmacSHA256";
    SecretKeyHelper secretKeyHelper = new SecretKeyHelper(algorithm);
    SecretKey hmacKey = secretKeyHelper.generateSecretKey(256);
    SecretKey aesKey = secretKeyHelper.generateSecretKey(256);
    assertThat(hmacKey.getEncoded()).hasSize(256 / 8).isNotNull();
    assertThat(aesKey.getEncoded()).hasSize(256 / 8).isNotNull();
  }

  @Test
  void generateSecretKey3() throws GeneralSecurityException {
    String algorithm = "HmacSHA256";
    SecretKeyHelper secretKeyHelper = new SecretKeyHelper(algorithm);
    SecretKey hmacKey = secretKeyHelper.generateSecretKey(256, new SecureRandom());
    assertThat(hmacKey.getEncoded()).hasSize(256 / 8).isNotNull();
  }


}