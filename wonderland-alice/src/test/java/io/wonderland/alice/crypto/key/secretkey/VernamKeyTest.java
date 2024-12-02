package io.wonderland.alice.crypto.key.secretkey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.VernamKeyASN1Codec;
import io.wonderland.alice.exception.ExceptionMessages;
import org.junit.jupiter.api.Test;

class VernamKeyTest {

  @Test
  void constructors() {
    VernamKey key = new VernamKey(27, new byte[]{3, 12});
    assertThat(key.getModulus()).isEqualTo(27);
    assertThat(key.getKey()).hasSize(2).containsExactly(3, 12);

    VernamKey key1 = new VernamKey(new byte[]{32, 12});
    assertThat(key1.getModulus()).isEqualTo(CharsetsUtils.getDefaultAlphabetSize());
    assertThat(key1.getKey()).hasSize(2).containsExactly(32, 12);

    //negative test
    assertThatThrownBy(() -> new VernamKey(0, new byte[0]))
        .withFailMessage(ExceptionMessages.KEY_MODULUS_NOT_VALID)
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new VernamKey(1, new byte[0]))
        .withFailMessage(ExceptionMessages.KEY_ARGS_NOT_VALID)
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new VernamKey(new byte[0]))
        .withFailMessage(ExceptionMessages.KEY_ARGS_NOT_VALID)
        .isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  void getAlgorithm() {
    VernamKey key = new VernamKey(27, new byte[]{3, 12});
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.VERNAM.getName());
  }

  @Test
  void getFormat() {
    VernamKey key = new VernamKey(27, new byte[]{3, 12});
    assertThat(key.getFormat()).isEqualTo(Algorithms.VERNAM.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    VernamKey key = new VernamKey(27, new byte[]{3, 12});
    assertThat(key.getEncoded()).isEqualTo(VernamKeyASN1Codec.getInstance().encoder().apply(key));
  }

}