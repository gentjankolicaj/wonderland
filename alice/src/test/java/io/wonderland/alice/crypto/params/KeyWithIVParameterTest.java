package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import org.junit.jupiter.api.Test;

class KeyWithIVParameterTest {

  @Test
  void getKeyWithIv() {
    assertThat(
        new KeyWithIVParameter<>(new CaesarKey(13), new byte[]{10, -1}).getIv()).containsExactly(10,
        -1);
    assertThat(
        new KeyWithIVParameter<>(new CaesarKey(12), new byte[0]).getKey().getShift()).isEqualTo(12);
    assertThatThrownBy(() -> new KeyWithIVParameter<>(null, null)).isInstanceOf(
        NullPointerException.class);
    assertThat(new KeyWithIVParameter<>(new CaesarKey(12), new byte[0]).getIv()).hasSize(0);
  }


}