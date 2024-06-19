package io.wonderland.rh.key.pair;

import java.security.KeyPair;
import javafx.scene.layout.BorderPane;

public abstract class AbstractKeyPairPane<A> extends BorderPane {

  protected abstract KeyPair getKeyPair();

  protected abstract boolean removeKeyPair();

  public abstract String getKeyPairAlgorithm();

  public abstract A getCipherKey();

}
