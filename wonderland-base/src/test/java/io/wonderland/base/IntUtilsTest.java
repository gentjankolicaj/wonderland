package io.wonderland.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IntUtilsTest {

  @Test
  void parseInt() {
    assertThat(IntUtils.parseInt(new byte[]{1})).isEqualTo(1);
    assertThat(IntUtils.parseInt(new byte[]{1, 2})).isEqualTo(12);
    assertThat(IntUtils.parseInt(new byte[]{1, 2, 3})).isEqualTo(123);
    assertThat(IntUtils.parseInt(new byte[]{1, 2, 3, 4})).isEqualTo(1234);
    assertThat(IntUtils.parseInt(new byte[]{12, 3, 4})).isEqualTo(1234);
    assertThat(IntUtils.parseInt(new byte[]{12, 34})).isEqualTo(154);
  }


  @Test
  void getIntBE() {
    assertThat(IntUtils.getIntBE(new byte[]{1})).isEqualTo(1);
    assertThat(IntUtils.getIntBE(new byte[]{1, 2})).isEqualTo(258);
    assertThat(IntUtils.getIntBE(new byte[]{1, 2, 3})).isEqualTo(66051);
    assertThat(IntUtils.getIntBE(new byte[]{1, 2, 3, 4})).isEqualTo(16909060);
  }

  @Test
  void getOptimalBytesBE() {
    assertThat(IntUtils.getOptimalBytesBE(10)).hasSize(1).contains(10);
    assertThat(IntUtils.getOptimalBytesBE(100)).hasSize(1).contains(100);
    assertThat(IntUtils.getOptimalBytesBE(500)).hasSize(2).contains(1, 244);
    assertThat(IntUtils.getOptimalBytesBE(66051)).hasSize(3).contains(1, 2, 3);
  }


  @Test
  void getBytesBE() {
    assertThat(IntUtils.getBytesBE(10)).hasSize(4).contains(0, 0, 0, 10);
    assertThat(IntUtils.getBytesBE(100)).hasSize(4).contains(0, 0, 0, 100);
    assertThat(IntUtils.getBytesBE(500)).hasSize(4).contains(0, 0, 1, 244);
    assertThat(IntUtils.getBytesBE(66051)).hasSize(4).contains(0, 1, 2, 3);
  }

  @Test
  void parseIntList() {
    assertThat(IntUtils.parseIntList("0", "12", "123", "66051")).hasSize(4)
        .contains(0, 12, 123, 66051);
  }

  @Test
  void getIntListBE() {
    assertThat(IntUtils.getIntListBE(new byte[]{1})).hasSize(1).contains(1);
    assertThat(IntUtils.getIntListBE(new byte[]{1, 2})).hasSize(1).contains(258);
    assertThat(IntUtils.getIntListBE(new byte[]{1, 2, 3})).hasSize(1).contains(66051);
    assertThat(IntUtils.getIntListBE(new byte[]{1, 2, 3, 4})).hasSize(1).contains(16909060);
    assertThat(IntUtils.getIntListBE(new byte[]{1, 2, 3, 4, 2})).hasSize(2).contains(16909060, 2);
  }


}