package io.wonderland.alice.exception;

import javax.crypto.BadPaddingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GenericPaddingException extends BadPaddingException {

  private final Throwable throwable;

  public GenericPaddingException(String message, Throwable throwable) {
    super(message);
    this.throwable = throwable;
  }
}
