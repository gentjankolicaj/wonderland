package io.wonderland.alice.crypto.cipher.asymmetric;


import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.keypair.gen.RSAKeyGenerator;
import java.security.KeyPair;
import org.junit.jupiter.api.Test;

class RSAKeyGeneratorTest {

  @Test
  void generate() {
    RSAKeyGenerator rsaKeyGenerator = new RSAKeyGenerator();
    assertThat(rsaKeyGenerator.generate(23, 41, 7)).isInstanceOf(KeyPair.class);
  }


}