package io.wonderland.rh.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {

  @Test
  void getCharsetClasses() {
    assertThat(ReflectionUtils.getCharsetClasses()).isNotEmpty();
  }
}