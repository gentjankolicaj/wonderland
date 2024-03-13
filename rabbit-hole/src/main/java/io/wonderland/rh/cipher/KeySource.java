package io.wonderland.rh.cipher;

import java.security.Key;

public interface KeySource<T extends Key> {
  T getKey();
  boolean removeKey();
}
