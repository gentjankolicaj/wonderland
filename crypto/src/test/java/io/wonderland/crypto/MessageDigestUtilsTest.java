package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorException;
import org.junit.jupiter.api.Test;

@Slf4j
class MessageDigestUtilsTest extends AbstractTest {


  @Test
  void createDigest() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    MessageDigest messageDigest = MessageDigestUtils.createDigest(CSP_NAME, "SHA-256");
    assertThat(messageDigest).isNotNull();
    assertThat(messageDigest.digest(input)).isNotNull().hasSize(256 / 8);
  }

  @Test
  void digest() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    assertThat(MessageDigestUtils.digest(CSP_NAME, "SHA-256", input))
        .isNotNull().hasSize(256 / 8);

    assertThatThrownBy(() -> MessageDigestUtils.digest(CSP_NAME, "SHA-256", null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createDigestCalculator() throws IOException, GeneralSecurityException, OperatorException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    byte[] digestedInput = MessageDigestUtils.digest(CSP_NAME, "SHA-256", input);
    assertThat(MessageDigestUtils.createDigestCalculator(CSP_NAME, "SHA-256")).isNotNull();

    DigestCalculator digestCalculator = MessageDigestUtils.createDigestCalculator(CSP_NAME,
        "SHA-256");
    OutputStream os = digestCalculator.getOutputStream();

    //write input to digest calculator
    os.write(input);
    os.close();

    //digest input & assert
    assertThat(digestCalculator.getDigest()).containsExactly(digestedInput);
  }


}