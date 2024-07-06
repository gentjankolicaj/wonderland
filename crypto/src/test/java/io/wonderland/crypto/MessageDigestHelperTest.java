package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

@Slf4j
class MessageDigestHelperTest extends AbstractTest {


  @Test
  void constructors() throws GeneralSecurityException {
    String algorithm = "sha-256";
    MessageDigestHelper messageDigestHelper = new MessageDigestHelper(algorithm);
    assertThat(messageDigestHelper.getAlgorithm()).isEqualTo(algorithm);
    assertThat(messageDigestHelper.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(messageDigestHelper.getMd()).isNotNull();

    MessageDigestHelper messageDigestHelper1 = new MessageDigestHelper(CSP_NAME, algorithm);
    assertThat(messageDigestHelper1.getAlgorithm()).isEqualTo(algorithm);
    assertThat(messageDigestHelper1.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(messageDigestHelper1.getMd()).isNotNull();
  }


  @Test
  void digest() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    String algorithm = "sha-256";
    MessageDigestHelper messageDigestHelper = new MessageDigestHelper(algorithm);
    assertThat(messageDigestHelper.digest(input)).isNotNull().hasSize(256 / 8);

    assertThatThrownBy(() -> messageDigestHelper.digest(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void update() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    byte[] doubleInput = ArrayUtils.addAll(input, input);
    String algorithm = "sha-256";
    MessageDigestHelper messageDigestHelper = new MessageDigestHelper(algorithm);

    //call update method
    messageDigestHelper.update(input);
    messageDigestHelper.update(input);
    assertThat(messageDigestHelper.digest()).isEqualTo(messageDigestHelper.digest(doubleInput))
        .hasSize(256 / 8);

  }


}