package io.wonderland.alice.crypto.key;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.Algorithms;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GenericSecretKeyTest {

  @Test
  void getAlgorithm() {
    GenericSecretKey byteKey = new GenericSecretKey(new byte[]{1});
    assertThat(byteKey.getAlgorithm()).isEqualTo(Algorithms.GENERIC.getName());
  }

  @Test
  void getFormat() {
    GenericSecretKey byteKey = new GenericSecretKey(new byte[]{1});
    assertThat(byteKey.getFormat()).isEqualTo(Algorithms.GENERIC.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    GenericSecretKey byteKey = new GenericSecretKey(new byte[]{1});
    assertThat(byteKey.getEncoded()).hasSize(1).containsExactly(1);

    byte[] key = "1qazxsw23edc4rfv5tgb6yhnmju78ik,.l/';".getBytes();
    GenericSecretKey byteKey1 = new GenericSecretKey(key);
    assertThat(byteKey1.getEncoded())
        .hasSameSizeAs(key)
        .containsExactly(key)
        .isNot(new Condition<>(encoded -> encoded == key,
            "Same array in heap,arrays must be different"));
  }


}