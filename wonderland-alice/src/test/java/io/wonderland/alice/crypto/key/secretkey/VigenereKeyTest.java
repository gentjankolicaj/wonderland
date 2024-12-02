package io.wonderland.alice.crypto.key.secretkey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.VigenereKeyASN1Codec;
import io.wonderland.alice.exception.ExceptionMessages;
import org.junit.jupiter.api.Test;

class VigenereKeyTest {

  @Test
  void constructors() {
    VigenereKey key = new VigenereKey(27, new byte[]{3, 12});
    assertThat(key.getModulus()).isEqualTo(27);
    assertThat(key.getKey()).hasSize(2).containsExactly(3, 12);

    VigenereKey key1 = new VigenereKey(new byte[]{32, 12});
    assertThat(key1.getModulus()).isEqualTo(CharsetsUtils.getDefaultAlphabetSize());
    assertThat(key1.getKey()).hasSize(2).containsExactly(32, 12);

    //negative test
    assertThatThrownBy(() -> new VigenereKey(0, new byte[0]))
        .withFailMessage(ExceptionMessages.KEY_MODULUS_NOT_VALID)
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new VigenereKey(1, new byte[0]))
        .withFailMessage(ExceptionMessages.KEY_ARGS_NOT_VALID)
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new VigenereKey(new byte[0]))
        .withFailMessage(ExceptionMessages.KEY_ARGS_NOT_VALID)
        .isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  void getAlgorithm() {
    VigenereKey key = new VigenereKey(27, new byte[]{3, 12});
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.VIGENERE.getName());
  }

  @Test
  void getFormat() {
    VigenereKey key = new VigenereKey(27, new byte[]{3, 12});
    assertThat(key.getFormat()).isEqualTo(Algorithms.VIGENERE.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    VigenereKey key = new VigenereKey(27, new byte[]{3, 12});
    assertThat(key.getEncoded()).isEqualTo(VigenereKeyASN1Codec.getInstance().encoder().apply(key));
  }

}