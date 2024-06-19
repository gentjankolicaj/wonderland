package io.wonderland.rh.base.codec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import org.junit.jupiter.api.Test;

class CodecAlgTest {

  @Test
  void getRadix() {
    CodecAlg<?, ?, ?, ?> codecAlg = new CodecAlg<>() {
      @Override
      public Function<Object, Object> encode() {
        return null;
      }

      @Override
      public Function<Object, Object> decode() {
        return null;
      }
    };
    assertThat(codecAlg.getName()).isEqualTo("CodecAlg");
  }

  @Test
  void getName() {
    CodecAlg<?, ?, ?, ?> codecAlg = new CodecAlg<>() {
      @Override
      public Function<Object, Object> encode() {
        return null;
      }

      @Override
      public Function<Object, Object> decode() {
        return null;
      }
    };
    assertThat(codecAlg.getRadix()).isEqualTo(2);
  }
}