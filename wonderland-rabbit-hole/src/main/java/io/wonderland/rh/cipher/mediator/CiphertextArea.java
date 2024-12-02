package io.wonderland.rh.cipher.mediator;

import io.wonderland.rh.base.fx.CodecTextArea;
import io.wonderland.rh.base.mediator.Component;
import io.wonderland.rh.base.mediator.Mediator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class CiphertextArea extends CodecTextArea implements Component<String> {

  public static final String KEY = "CiphertextArea";
  private Mediator<String> mediator;

  @Override
  public String getKey() {
    return KEY;
  }

  @Override
  public byte[] getValue() {
    String text = getText();
    return getCodecAlg().decode().apply(text);
  }

  public void setValue(byte[] value) {
    //Encode text using codec alg
    String encoded = getCodecAlg().encode().apply(value);

    //set text
    setText(encoded);
  }

}
