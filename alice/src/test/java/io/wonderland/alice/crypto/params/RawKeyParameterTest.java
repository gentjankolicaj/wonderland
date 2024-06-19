package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import java.security.Key;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RawKeyParameterTest {

  @Test
  void getKey() {
    Key caesarKey = new CaesarKey(13);
    RawKeyParameter keyParam = new RawKeyParameter(caesarKey.getEncoded());

    //assertions
    assertThat(keyParam.getKey()).is(
        new Condition<>(encoded -> encoded[0] == caesarKey.getEncoded()[0],
            "Param key is same object as initial key"));

    assertThatThrownBy(() -> new RawKeyParameter(null)).withFailMessage(
            "Key encoded must not be null.")
        .isInstanceOf(NullPointerException.class);
  }
}