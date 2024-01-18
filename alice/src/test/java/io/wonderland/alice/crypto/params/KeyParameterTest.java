package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.cipher.key.CaesarKey;
import java.security.Key;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class KeyParameterTest {

  @Test
  void getKey() {
    Key caesarKey = new CaesarKey(13);
    KeyParameter keyParam = new KeyParameter(caesarKey.getEncoded());

    //assertions
    assertThat(keyParam.getKey()).is(
        new Condition<>(encoded -> encoded[0] == caesarKey.getEncoded()[0], "Param key is same object as initial key"));

    assertThatThrownBy(() -> new KeyParameter(null)).withFailMessage("Key encoded must not be null.")
        .isInstanceOf(NullPointerException.class);
  }
}