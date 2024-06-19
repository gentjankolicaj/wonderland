package io.wonderland.rh.cipher.mediator;


import io.wonderland.rh.base.fx.CharsetTextArea;
import io.wonderland.rh.base.mediator.Component;
import io.wonderland.rh.base.mediator.Mediator;
import lombok.Setter;


@Setter
public class PlaintextArea extends CharsetTextArea implements Component<String> {

  public static final String KEY = "PlaintextArea";
  private Mediator<String> mediator;

  @Override
  public String getKey() {
    return KEY;
  }

  @Override
  public byte[] getValue() {
    return getText().getBytes(getCharset());
  }

  public void setValue(byte[] value) {
    //construct string using current charset
    String text = new String(value, getCharset());
    //set text
    setText(text);
  }

}
