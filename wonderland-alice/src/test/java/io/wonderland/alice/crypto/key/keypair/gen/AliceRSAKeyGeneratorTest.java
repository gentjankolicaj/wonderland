package io.wonderland.alice.crypto.key.keypair.gen;


import static org.assertj.core.api.Assertions.assertThat;

import java.security.KeyPair;
import org.junit.jupiter.api.Test;

class AliceRSAKeyGeneratorTest {

  @Test
  void generate() {
    AliceRSAKeyGenerator aliceRsaKeyGenerator = new AliceRSAKeyGenerator();
    assertThat(aliceRsaKeyGenerator.generate(23, 41, 7)).isInstanceOf(KeyPair.class);
  }


}