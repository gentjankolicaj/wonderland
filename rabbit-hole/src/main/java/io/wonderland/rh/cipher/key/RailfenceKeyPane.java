package io.wonderland.rh.cipher.key;

import io.wonderland.alice.crypto.cipher.key.RailfenceKey;
import java.util.function.Consumer;


public final class RailfenceKeyPane extends DefaultKeyPane<RailfenceKey> {

  private static final String KEY_LABEL = "Railfence key (rails)";

  public RailfenceKeyPane(Consumer<?> cipherStateConsumer) {
    super(KEY_LABEL, cipherStateConsumer);
  }

  @Override
  protected RailfenceKey getCipherKey() {
    return new RailfenceKey(Integer.parseInt(textArea.getText()));
  }
}
