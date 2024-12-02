package io.wonderland.rh.key.secret;

import javafx.scene.layout.BorderPane;
import javax.crypto.SecretKey;

public abstract class AbstractKeyPane<A> extends BorderPane {

  protected abstract SecretKey getKey();

  protected abstract boolean removeKey();

  public abstract String getKeyAlgorithm();

  public abstract A getCipherKey();

}
