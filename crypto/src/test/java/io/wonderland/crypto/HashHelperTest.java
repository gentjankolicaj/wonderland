package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

@Slf4j
class HashHelperTest extends AbstractTest {


  @Test
  void constructors() throws GeneralSecurityException {
    String algorithm = "sha-256";
    HashHelper hashHelper = new HashHelper(algorithm);
    assertThat(hashHelper.getAlgorithm()).isEqualTo(algorithm);
    assertThat(hashHelper.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(hashHelper.getMd()).isNotNull();

    HashHelper hashHelper1 = new HashHelper(CSP_NAME, algorithm);
    assertThat(hashHelper1.getAlgorithm()).isEqualTo(algorithm);
    assertThat(hashHelper1.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(hashHelper1.getMd()).isNotNull();
  }


  @Test
  void digest() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    String algorithm = "sha-256";
    HashHelper hashHelper = new HashHelper(algorithm);
    assertThat(hashHelper.digest(input)).isNotNull().hasSize(256 / 8);

    assertThatThrownBy(() -> hashHelper.digest(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void update() throws GeneralSecurityException {
    byte[] input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_".getBytes();
    byte[] doubleInput = ArrayUtils.addAll(input, input);
    String algorithm = "sha-256";
    HashHelper hashHelper = new HashHelper(algorithm);

    //call update method
    hashHelper.update(input);
    hashHelper.update(input);
    assertThat(hashHelper.digest()).isEqualTo(hashHelper.digest(doubleInput))
        .hasSize(256 / 8);

  }


}