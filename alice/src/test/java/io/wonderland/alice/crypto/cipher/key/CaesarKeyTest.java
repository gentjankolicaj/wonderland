package io.wonderland.alice.crypto.cipher.key;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CaesarKeyTest {

  @Test
  void getAlgorithm() {
    CaesarKey key = new CaesarKey(1);
    assertThat(key.getAlgorithm()).isEqualTo("Caesar");
  }

  @Test
  void getFormat() {
    CaesarKey key = new CaesarKey(1);
    assertThat(key.getFormat()).isEqualTo(KeyFormats.INT.name());
  }

  @Test
  void getEncoded() {
    CaesarKey key = new CaesarKey(1);
    assertThat(key.getEncoded()).hasSize(1).containsExactly(1);
  }


}