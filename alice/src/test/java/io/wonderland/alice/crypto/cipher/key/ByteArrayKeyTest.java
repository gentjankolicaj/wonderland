package io.wonderland.alice.crypto.cipher.key;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ByteArrayKeyTest {

  @Test
  void getAlgorithm() {
    ByteArrayKey byteArrayKey = new ByteArrayKey(new byte[]{1});
    assertThat(byteArrayKey.getAlgorithm()).isEqualTo("raw");
  }

  @Test
  void getFormat() {
    ByteArrayKey byteArrayKey = new ByteArrayKey(new byte[]{1});
    assertThat(byteArrayKey.getFormat()).isEqualTo(KeyFormats.BYTE_ARRAY.name());
  }

  @Test
  void getEncoded() {
    ByteArrayKey byteArrayKey = new ByteArrayKey(new byte[]{1});
    assertThat(byteArrayKey.getEncoded()).hasSize(1).containsExactly(1);

    byte[] key = "1qazxsw23edc4rfv5tgb6yhnmju78ik,.l/';".getBytes();
    ByteArrayKey byteArrayKey1 = new ByteArrayKey(key);
    assertThat(byteArrayKey1.getEncoded())
        .hasSameSizeAs(key)
        .containsExactly(key)
        .isNot(new Condition<>(encoded -> encoded == key, "Same array in heap,arrays must be different"));
  }


}