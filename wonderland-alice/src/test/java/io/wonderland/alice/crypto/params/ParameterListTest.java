package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import java.util.List;
import org.junit.jupiter.api.Test;

class ParameterListTest {


  @Test
  void testConstructor() {
    assertThat((new ParameterList())).isEmpty();
    assertThat(new ParameterList(new RawKeyParameter(new byte[]{}))).hasSize(1);
    assertThat(new ParameterList(
        List.of(new RawKeyParameter(new byte[]{}), new KeyParameter<>(new CaesarKey(13))))).hasSize(
        2);
  }

}