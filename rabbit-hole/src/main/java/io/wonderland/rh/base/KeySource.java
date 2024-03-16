package io.wonderland.rh.base;

import java.security.Key;

public interface KeySource<T extends Key> {
  T getKey();
  boolean removeKey();
}
