package io.wonderland.alice.crypto.random;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class CommonRandomTest {

  @Test
  void secLong() {
    assertThat(CommonRandom.secLong(3, 10)).isBetween(3L, 10L);
    assertThat(CommonRandom.secLong(333, 10000)).isBetween(333L, 10000L);
    assertThat(CommonRandom.secLong(123_345_678, 987_654_321)).isBetween(123_345_678L,
        987_654_321L);
  }

  @Test
  void secBigInteger() {
    assertThat(CommonRandom.secBigInteger(BigInteger.valueOf(3), BigInteger.valueOf(10)))
        .isBetween(BigInteger.valueOf(3L), BigInteger.valueOf(10L));
    assertThat(CommonRandom.secBigInteger(BigInteger.valueOf(333), BigInteger.valueOf(10000)))
        .isBetween(BigInteger.valueOf(333L), BigInteger.valueOf(10000L));
    assertThat(CommonRandom.secBigInteger(BigInteger.valueOf(123_345_678),
        BigInteger.valueOf(987_654_321)))
        .isBetween(BigInteger.valueOf(123_345_678L), BigInteger.valueOf(987_654_321L));
  }
}