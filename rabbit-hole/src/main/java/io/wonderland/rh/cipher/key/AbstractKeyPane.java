package io.wonderland.rh.cipher.key;

import io.wonderland.rh.base.KeySource;
import javafx.scene.layout.BorderPane;

public abstract class AbstractKeyPane<A> extends BorderPane implements KeySource {

  public abstract String getKeyLabel();

   public abstract A getCipherKey();

}
