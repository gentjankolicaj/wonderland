package io.wonderland.rh.cipher.key;

import io.wonderland.alice.crypto.cipher.key.AffineKey;
import java.util.Map;
import java.util.function.Consumer;
import javafx.scene.control.TextField;

public final class AffineKeyPane extends TextFieldKeyPane<AffineKey> {

  private static final String KEY_LABEL = "Affine key (a,b)";
  private static final String A = "a";
  private static final String B = "b";

  public AffineKeyPane(Consumer cipherStateConsumer) {
    super(KEY_LABEL, cipherStateConsumer, Map.of(A, new TextField(), B, new TextField()));
  }


  @Override
 public String getKeyLabel() {
    return KEY_LABEL;
  }


  @Override
 public AffineKey getKey() {
    TextField aTF = super.textFieldMap.get(A);
    TextField bTF = super.textFieldMap.get(B);
    return new AffineKey(Integer.parseInt(aTF.getText()), Integer.parseInt(bTF.getText()));
  }

}
