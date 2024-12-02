package io.wonderland.alice.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GCDTest {

  @Test
  void gcdInt() {
    assertThat(GCD.gcdInt(1, 12)).isOne();
    assertThat(GCD.gcdInt(1, 3)).isOne();
    assertThat(GCD.gcdInt(3, 5)).isOne();
    assertThat(GCD.gcdInt(89, 97)).isOne();
    assertThat(GCD.gcdInt(773, 859)).isOne();
  }

  @Test
  void gcdLong() {
    assertThat(GCD.gcdLong(1, 12)).isOne();
    assertThat(GCD.gcdLong(1, 3)).isOne();
    assertThat(GCD.gcdLong(3, 5)).isOne();
    assertThat(GCD.gcdLong(89, 97)).isOne();
    assertThat(GCD.gcdLong(773, 859)).isOne();
  }
}