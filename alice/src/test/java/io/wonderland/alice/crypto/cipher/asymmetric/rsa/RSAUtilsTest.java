package io.wonderland.alice.crypto.cipher.asymmetric.rsa;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class RSAUtilsTest {


  @Test
  void qInv() {
    //97 mod(89) ≡ 78 (mod 89)
    assertThat(RSAUtils.qInv(BigInteger.valueOf(89), BigInteger.valueOf(97))).isEqualTo(
        BigInteger.valueOf(78));
  }

}