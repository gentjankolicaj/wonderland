package io.wonderland.alice.crypto.key.secretkey;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.RailfenceKeyASN1Codec;
import org.junit.jupiter.api.Test;

class RailfenceKeyTest {

  @Test
  void getAlgorithm() {
    RailfenceKey key = new RailfenceKey(1);
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.RAILFENCE.getName());
  }

  @Test
  void getFormat() {
    RailfenceKey key = new RailfenceKey(1);
    assertThat(key.getFormat()).isEqualTo(Algorithms.RAILFENCE.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    RailfenceKey key = new RailfenceKey(1);
    assertThat(key.getEncoded()).hasSize(3)
        .isEqualTo(RailfenceKeyASN1Codec.getInstance().encoder().apply(key));
  }


}