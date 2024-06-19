package io.wonderland.rh.base.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.rh.base.fx.CodecDropdown;
import java.util.List;
import org.junit.jupiter.api.Test;

class CodecDropdownTest {

  @Test
  void getAllCodecAlgs() {
    List<CodecAlg> codecAlgs = CodecDropdown.getAllCodecAlgs();
    assertThat(codecAlgs).isNotEmpty();
  }
}