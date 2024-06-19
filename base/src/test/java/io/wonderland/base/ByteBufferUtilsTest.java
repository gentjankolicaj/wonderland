package io.wonderland.base;

import java.nio.ByteBuffer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ByteBufferUtilsTest {

  @Test
  void getOptBytes() {
    byte[] empty = new byte[0];
    Assertions.assertThat(ByteBufferUtils.getBytes(ByteBuffer.wrap(empty))).isEmpty();
    Assertions.assertThat(ByteBufferUtils.getOptBytes(ByteBuffer.wrap(empty))).isEmpty();

    byte[] content = new byte[]{1, 2, 3, 4, 5, 6, 7};
    Assertions.assertThat(ByteBufferUtils.getBytes(ByteBuffer.wrap(content))).hasSize(7);
    Assertions.assertThat(ByteBufferUtils.getOptBytes(ByteBuffer.wrap(content))).isNotEmpty();
    Assertions.assertThat(ByteBufferUtils.getOptBytes(ByteBuffer.wrap(content))).isPresent();
  }

}