package io.wonderland.rh.key.secret;

import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import java.util.Map;
import javafx.scene.control.TextField;

public final class AffineKeyPane extends TextFieldKeyPane<AffineKey> {

  private static final String KEY_LABEL = "Affine key (a,b)";
  private static final String A = "a";
  private static final String B = "b";

  public AffineKeyPane(EitherKeyObserver eitherKeyObserver) {
    super(KEY_LABEL, eitherKeyObserver, Map.of(A, new TextField(), B, new TextField()));
  }


  @Override
  public String getKeyAlgorithm() {
    return KEY_LABEL;
  }


  @Override
  public AffineKey getKey() {
    TextField aTF = super.textFieldMap.get(A);
    TextField bTF = super.textFieldMap.get(B);
    return new AffineKey(Integer.parseInt(aTF.getText()), Integer.parseInt(bTF.getText()));
  }

}
