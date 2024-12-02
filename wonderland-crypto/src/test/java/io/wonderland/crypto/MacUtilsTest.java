package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class MacUtilsTest extends AbstractTest {

  @Test
  void mac() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();

    //HMAC test (256 bits aes / 8 bit per byte)
    SecretKey hmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256");
    assertThat(MacUtils.mac(CSP_NAME, "HmacSHA256", hmacKey, input))
        .isNotNull().hasSize(256 / 8);

    //AESCMAC test (128 bits aes / 8 bit per byte)
    SecretKey cmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");
    assertThat(MacUtils.mac(CSP_NAME, "AESCMAC", cmacKey, input))
        .isNotNull().hasSize(128 / 8);
  }

  @Test
  void createMac() throws GeneralSecurityException {
    SecretKey hmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256");
    assertThat(MacUtils.createMac(CSP_NAME, "HmacSHA256", hmacKey)).isNotNull();
  }


}