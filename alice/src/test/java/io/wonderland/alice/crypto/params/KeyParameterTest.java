package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import org.junit.jupiter.api.Test;

class KeyParameterTest {

  @Test
  void getKey() {
    assertThat(new KeyParameter<>(new CaesarKey(12)).getKey().getShift()).isEqualTo(12);
    assertThatThrownBy(() -> new KeyParameter<>(null)).isInstanceOf(NullPointerException.class);
  }
}