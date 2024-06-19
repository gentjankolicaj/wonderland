package io.wonderland.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CharUtilsTest {


  @Test
  void getOptimalBytesBE() {
    assertThat(CharUtils.getOptimalBytesBE('9')).hasSize(1).contains(57);
    assertThat(CharUtils.getOptimalBytesBE((char) 9)).hasSize(1).contains(9);
    assertThat(CharUtils.getOptimalBytesBE((char) 500)).hasSize(2).contains(1, 244);
  }


  @Test
  void getBytesBE() {
    assertThat(CharUtils.getBytesBE('9')).hasSize(2).contains(0, 57);
    assertThat(CharUtils.getBytesBE((char) 9)).hasSize(2).contains(0, 9);
    assertThat(CharUtils.getBytesBE((char) 500)).hasSize(2).contains(1, 244);
  }

}