package io.wonderland.rh.key.secret;

import io.wonderland.alice.crypto.key.secretkey.RailfenceKey;
import io.wonderland.rh.base.observer.EitherKeyObserver;


public final class RailfenceKeyPane extends DefaultSecretKeyPane<RailfenceKey> {

  private static final String KEY_LABEL = "Railfence key (rails)";

  public RailfenceKeyPane(EitherKeyObserver eitherKeyObserver) {
    super(KEY_LABEL, eitherKeyObserver);
  }

  @Override
  public RailfenceKey getKey() {
    return new RailfenceKey(Integer.parseInt(textArea.getText()));
  }

}
