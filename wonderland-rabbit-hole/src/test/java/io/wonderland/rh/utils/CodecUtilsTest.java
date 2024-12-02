package io.wonderland.rh.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodecUtilsTest {

  @Test
  void decodeBase10() {
    String blankData = "86 -81 126 -45 -11 -48 63 -57 -70 99 55 -103 115 -4 62 43 ";
    byte[] blankDecoded = CodecUtils.decodeBase10(blankData, ' ');
    assertThat(blankDecoded).contains(-70);

    String commaData = "77,29,-125,-21,-29,-94,-80,-51,21,89,18,-76,105,-53,109,-125,-28,-17,116,33,51,-45,-1,20,95,50,-28,48,116,103,13,-8,";
    byte[] commaDecoded = CodecUtils.decodeBase10(commaData, ',');
    assertThat(commaDecoded).contains(-17);
  }
}