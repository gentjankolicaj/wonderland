package io.wonderland.rh.cipher.key;

import io.wonderland.alice.crypto.cipher.key.CaesarKey;
import java.util.function.Consumer;


public final class CaesarKeyPane extends DefaultKeyPane<CaesarKey> {

  private static final String KEY_LABEL = "Caesar key (shifts)";

  public CaesarKeyPane(Consumer cipherStateConsumer) {
    super(KEY_LABEL, cipherStateConsumer);
  }

  @Override
  protected CaesarKey getCipherKey() {
    return new CaesarKey(Integer.parseInt(textArea.getText()));
  }
}
