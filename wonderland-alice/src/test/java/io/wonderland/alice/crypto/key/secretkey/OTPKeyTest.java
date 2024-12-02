package io.wonderland.alice.crypto.key.secretkey;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.OTPKeyASN1Codec;
import java.util.List;
import org.junit.jupiter.api.Test;

class OTPKeyTest {

  @Test
  void constructors() {
    OTPKey key = new OTPKey(27, 3, 12);
    assertThat(key.getModulus()).isEqualTo(27);
    assertThat(key.getCodeKeys()).hasSize(2);

    OTPKey key1 = new OTPKey(27, List.of(3, 12));
    assertThat(key1.getModulus()).isEqualTo(27);
    assertThat(key1.getCodeKeys()).hasSize(2);

    OTPKey key2 = new OTPKey(new byte[]{3, 12});
    assertThat(key2.getModulus()).isEqualTo(CharsetsUtils.getDefaultAlphabetSize());
    assertThat(key2.getCodeKeys()).hasSize(1).contains(780);

    OTPKey key3 = new OTPKey(new byte[]{1, 2, 3, 4, 2});
    assertThat(key3.getModulus()).isEqualTo(CharsetsUtils.getDefaultAlphabetSize());
    assertThat(key3.getCodeKeys()).hasSize(2).contains(16909060, 2);

    OTPKey key4 = new OTPKey("10", "1", "2", "3", "4");
    assertThat(key4.getModulus()).isEqualTo(10);
    assertThat(key4.getCodeKeys()).hasSize(4).contains(1, 2, 3, 4);

  }

  @Test
  void getAlgorithm() {
    OTPKey key = new OTPKey(27, 3, 12);
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.OTP.getName());
  }

  @Test
  void getFormat() {
    OTPKey key = new OTPKey(27, 3, 12);
    assertThat(key.getFormat()).isEqualTo(Algorithms.OTP.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    OTPKey key = new OTPKey(27, 3, 12);
    assertThat(key.getEncoded()).isEqualTo(OTPKeyASN1Codec.getInstance().encoder().apply(key));
  }
}