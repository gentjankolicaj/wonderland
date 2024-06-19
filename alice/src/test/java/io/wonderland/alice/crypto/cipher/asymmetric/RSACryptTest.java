package io.wonderland.alice.crypto.cipher.asymmetric;


import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.cipher.asymmetric.rsa.RSACrypt;
import io.wonderland.alice.crypto.padding.RSAPadding;
import org.junit.jupiter.api.Test;


class RSACryptTest {


  @Test
  void constructor() {
    RSAPadding rsaPadding = new RSAPadding();
    RSACrypt crypt = new RSACrypt(rsaPadding);
    assertThat(crypt).isNotNull();
    assertThat(crypt.getRsaPadding()).isEqualTo(rsaPadding);
  }

}