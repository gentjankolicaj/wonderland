package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameters;
import java.security.Key;
import java.util.Objects;
import lombok.Getter;


@Getter
public final class WrappedKeyParameter<T extends Key> implements CipherParameters {

  private final T wrappedKey;

  public WrappedKeyParameter(T t) {
    Objects.requireNonNull(t, "Key being wrapped must not be null.");
    this.wrappedKey = t;
  }

}
