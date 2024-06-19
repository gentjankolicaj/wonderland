package io.wonderland.rh.base.fx;


import io.wonderland.rh.base.codec.Base10Blank;
import io.wonderland.rh.base.codec.CodecAlg;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodecTextArea extends TextArea {

  private CodecAlg<byte[], String, String, byte[]> codecAlg = new Base10Blank();

}
