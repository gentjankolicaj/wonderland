package io.wonderland.rh.base.fx;

import io.wonderland.rh.GlobalConstants;
import java.nio.charset.Charset;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharsetTextArea extends TextArea {

  private Charset charset = GlobalConstants.DEFAULT_CHARSET;

}
