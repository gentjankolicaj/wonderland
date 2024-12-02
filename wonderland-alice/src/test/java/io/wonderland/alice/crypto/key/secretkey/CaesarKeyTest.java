package io.wonderland.alice.crypto.key.secretkey;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.CaesarKeyASN1Codec;
import org.junit.jupiter.api.Test;

class CaesarKeyTest {

  @Test
  void getAlgorithm() {
    CaesarKey key = new CaesarKey(1);
    assertThat(key.getAlgorithm()).isEqualTo(Algorithms.CAESAR.getName());
  }

  @Test
  void getFormat() {
    CaesarKey key = new CaesarKey(1);
    assertThat(key.getFormat()).isEqualTo(Algorithms.CAESAR.getKeyFormat().getValue());
  }

  @Test
  void getEncoded() {
    CaesarKey key = new CaesarKey(1);
    assertThat(key.getEncoded()).hasSize(3)
        .isEqualTo(CaesarKeyASN1Codec.getInstance().encoder().apply(key));
  }


}