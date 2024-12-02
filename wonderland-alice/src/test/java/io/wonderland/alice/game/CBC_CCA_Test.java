package io.wonderland.alice.game;

import java.math.BigInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class CBC_CCA_Test {


  @Test
  void exercise1() {
    String ciphertext = "20814804c1767293b99f1d9cab3bc3e7 ac1e37bfb15599e5f40eef805488281d";
    String iv = ciphertext.substring(0, ciphertext.indexOf(" "));
    System.out.println("ct " + ciphertext + " len=" + ciphertext.length());
    System.out.println("ct-iv :" + iv + ", iv-len=" + iv.length());

    String iv1 = iv.substring(0, 24);
    System.out.println(iv1 + " , iv1-len=" + iv1.length());

    String xor100 = "000000000000000001000000";
    String xor500 = "000000000000000005000000";
    System.out.println(xor100 + " xor100-len=" + xor100.length());
    System.out.println(xor500 + " xor500-len=" + xor500.length());

    BigInteger bigInteger = new BigInteger(iv1, 16);
    bigInteger = bigInteger.xor(new BigInteger(xor100, 16)).xor(new BigInteger(xor500, 16));
    String modifiedCT = bigInteger.toString(16).concat(ciphertext.substring(24));
    System.out.println(modifiedCT + " mod-ct-len=" + modifiedCT.length());

    Assertions.assertThat(modifiedCT).isEqualTo(ciphertext);

  }

}
