package io.wonderland.rh.keygen;

import io.wonderland.common.Arrays;
import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.utils.LabelUtils;
import io.wonderland.rh.utils.ZxingUtils;
import java.security.Key;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@Setter
@Slf4j
public class KeyGeneratorPane extends BorderPane {

  private HTogglePane<RadioButton> keyFormatPane = new HTogglePane<>("Key format : ", 10,s -> new RadioButton(s),
      Map.of("qr-code", () -> setKeyQRCode(), "decimal", () -> setKeyText("Secret key",getSecretKey())));
  private SecretKey secretKey;

  public KeyGeneratorPane() {
    this.setTop(keyFormatPane);
    this.setPadding(new Insets(5,5,5,5));
  }


  public void update(SecretKey secretKey) {
    this.secretKey = secretKey;
    this.setKeyQRCode();
    this.keyFormatPane.selectToggle("qr-code");
  }

  private void setKeyQRCode() {
    try{
        this.setCenter(ZxingUtils.getImageView(this,getSecretKey()));
      } catch (Exception e) {
        log.error("", e);
      }
  }

  private HBox getKeyBox(String text, Key key){
    HBox hBox=new HBox();
    TextField textField=new TextField();
    textField.setText(Arrays.getStringValueOf(key.getEncoded(),','));
    Label label= LabelUtils.getTitle(text);
    HBox.setHgrow(textField, Priority.ALWAYS);
    hBox.getChildren().addAll(label,textField);
    hBox.setSpacing(10);
    return hBox;
  }


  private void setKeyText(String text,Key key) {
    this.setCenter(getKeyBox(text,key));
  }

}
