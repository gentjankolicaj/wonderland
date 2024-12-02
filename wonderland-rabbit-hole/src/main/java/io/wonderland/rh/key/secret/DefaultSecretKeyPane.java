package io.wonderland.rh.key.secret;


import io.wonderland.rh.base.observer.EitherKeyObserver;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSecretKeyPane<T extends Key> extends TextAreaKeyPane<T> {

  public DefaultSecretKeyPane(String keyLabel, EitherKeyObserver eitherKeyObserver) {
    super(keyLabel, eitherKeyObserver);
  }

  public DefaultSecretKeyPane(EitherKeyObserver eitherKeyObserver) {
    super("", eitherKeyObserver);
  }
}
