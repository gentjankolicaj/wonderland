package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.cipher.key.AffineKey;
import io.wonderland.alice.crypto.cipher.key.CaesarKey;
import java.security.Key;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WrappedKeyParameterTest {

  @Test
  void getWrappedKey() {
    Key caesarKey = new CaesarKey(13);
    Key affineKey = new AffineKey(2, 4, 5);
    WrappedKeyParameter<Key> caesarWrapped = new WrappedKeyParameter<>(caesarKey);
    WrappedKeyParameter<Key> affineWrapped = new WrappedKeyParameter<>(affineKey);

    //assertions
    assertThat(caesarWrapped.getWrappedKey()).is(
        new Condition<>(wrappedKey -> wrappedKey == caesarKey, "Wrapped key is same object as initial key"));
    assertThat(affineWrapped.getWrappedKey()).is(
        new Condition<>(wrappedKey -> wrappedKey == affineKey, "Wrapped key is same object as initial key"));
    assertThatThrownBy(() -> new WrappedKeyParameter<>(null)).withFailMessage("Key being wrapped must not be null.")
        .isInstanceOf(NullPointerException.class);
  }
}