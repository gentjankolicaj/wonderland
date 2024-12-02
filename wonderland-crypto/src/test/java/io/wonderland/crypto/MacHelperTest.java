package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class MacHelperTest extends AbstractTest {

  @Test
  void constructors() throws GeneralSecurityException {
    String algorithm = "HmacSHA256";
    MacHelper macHelper = new MacHelper(algorithm);
    assertThat(macHelper.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(macHelper.getAlgorithm()).isEqualTo(algorithm);
    assertThat(macHelper.getMac()).isNotNull();

    MacHelper macHelper1 = new MacHelper(CSP.SunJCE, algorithm);
    assertThat(macHelper1.getProvider()).isEqualTo(CSP.SunJCE);
    assertThat(macHelper1.getAlgorithm()).isEqualTo(algorithm);
    assertThat(macHelper1.getMac()).isNotNull();

  }

  @Test
  void mac() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    String algorithm = "HmacSHA256";
    MacHelper macHelper = new MacHelper(algorithm);
    SecretKey hmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256");

    //init mac
    macHelper.init(hmacKey);

    //HMAC test (256 bits aes / 8 bit per byte)
    //add message in buffer
    macHelper.update(input);
    assertThat(macHelper.mac())
        .isNotNull().hasSize(256 / 8);

    //HMAC test (256 bits aes / 8 bit per byte)
    assertThat(macHelper.mac(input))
        .isNotNull().hasSize(256 / 8);

    //AESCMAC test (128 bits aes / 8 bit per byte)
    SecretKey cmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");
    MacHelper macHelper1 = new MacHelper("AESCMAC");

    //init mac
    macHelper1.init(cmacKey);

    assertThat(macHelper1.mac(input))
        .isNotNull().hasSize(128 / 8);
  }

  @Test
  void createMac() throws GeneralSecurityException {
    String algorithm = "HmacSHA256";
    MacHelper macHelper = new MacHelper(algorithm);
    SecretKey hmacKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "HmacSHA256");
    assertThat(macHelper.createMac(hmacKey)).isNotNull();
  }


}