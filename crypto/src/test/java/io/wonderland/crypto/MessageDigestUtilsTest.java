package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorException;
import org.junit.jupiter.api.Test;

@Slf4j
class MessageDigestUtilsTest {

  static final String CSP = "BC";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }


  @Test
  void createDigest() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    MessageDigest messageDigest = MessageDigestUtils.createDigest(CSP, "SHA-256");
    assertThat(messageDigest).isNotNull();
    log.debug("Input : {}", input);
    log.debug("SHA-256 size {} : {} ", messageDigest.digest(input.getBytes()).length,
        messageDigest.digest(input.getBytes()));

  }

  @Test
  void computeDigest() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    assertThat(MessageDigestUtils.computeDigest(CSP, "SHA-256", input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("SHA-256 size {} : {} ",
        MessageDigestUtils.computeDigest(CSP, "SHA-256", input.getBytes()).length,
        MessageDigestUtils.computeDigest(CSP, "SHA-256", input.getBytes()));

    assertThatThrownBy(() -> MessageDigestUtils.computeDigest(CSP, "SHA-256", null)).isInstanceOf(
        IllegalArgumentException.class);
  }

  @Test
  void createDigestCalculator() throws IOException, GeneralSecurityException, OperatorException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] digestedInput = MessageDigestUtils.computeDigest(CSP, "SHA-256", input.getBytes());

    assertThat(MessageDigestUtils.createDigestCalculator(CSP, "SHA-256")).isNotNull();

    DigestCalculator digestCalculator = MessageDigestUtils.createDigestCalculator(CSP, "SHA-256");
    OutputStream os = digestCalculator.getOutputStream();

    //write input to digest calculator
    os.write(input.getBytes());
    os.close();

    //digest input & assert
    assertThat(digestCalculator.getDigest()).containsExactly(digestedInput);
  }

  @Test
  void computeMac() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";

    //HMAC test
    SecretKey hmacKey = KeyUtils.generateSecretKey("HmacSHA256");
    assertThat(
        MessageDigestUtils.computeMac(CSP, "HmacSHA256", hmacKey, input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("HmacSHA256 size {}",
        MessageDigestUtils.computeMac(CSP, "HmacSHA256", hmacKey, input.getBytes()).length);

    //AESCMAC test
    SecretKey cmacKey = KeyUtils.generateSecretKey("AES");
    assertThat(
        MessageDigestUtils.computeMac(CSP, "AESCMAC", cmacKey, input.getBytes())).isNotNull();
    log.debug("Input : {}", input);
    log.debug("HmacSHA256 size {}",
        MessageDigestUtils.computeMac(CSP, "AESCMAC", cmacKey, input.getBytes()).length);
  }


}