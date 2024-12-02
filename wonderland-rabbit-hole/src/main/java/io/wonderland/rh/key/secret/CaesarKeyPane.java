package io.wonderland.rh.key.secret;

import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import io.wonderland.rh.base.observer.EitherKeyObserver;


public final class CaesarKeyPane extends DefaultSecretKeyPane<CaesarKey> {

  private static final String KEY_LABEL = "Caesar key (shifts)";

  public CaesarKeyPane(EitherKeyObserver eitherKeyObserver) {
    super(KEY_LABEL, eitherKeyObserver);
  }

}
