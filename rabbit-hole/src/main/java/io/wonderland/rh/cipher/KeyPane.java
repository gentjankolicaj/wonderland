package io.wonderland.rh.cipher;

import java.security.Key;
import javafx.scene.layout.BorderPane;

public abstract class KeyPane<T extends Key> extends BorderPane {

  protected abstract String getKeyLabel();

  protected abstract T getCipherKey();

  protected abstract Key getKey();

  protected abstract boolean removeKey();

}
