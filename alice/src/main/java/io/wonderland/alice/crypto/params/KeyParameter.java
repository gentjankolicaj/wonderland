package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameter;
import java.security.Key;
import java.util.Objects;
import lombok.Getter;


@Getter
public final class KeyParameter<T extends Key> implements CipherParameter {

  private final T key;

  public KeyParameter(T t) {
    Objects.requireNonNull(t, "Key being wrapped must not be null.");
    this.key = t;
  }

}
