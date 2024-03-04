package io.wonderland.rh.cipher.key;

import io.wonderland.rh.cipher.KeySource;
import java.security.Key;
import javafx.scene.layout.BorderPane;
import org.checkerframework.checker.units.qual.K;

public abstract class AbstractKeyPane<A> extends BorderPane implements KeySource {

  public abstract String getKeyLabel();

   public abstract A getCipherKey();

}
