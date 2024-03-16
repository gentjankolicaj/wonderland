package io.wonderland.rh.keygen;

import io.wonderland.common.Arrays;
import io.wonderland.rh.base.common.HToggleBox;
import io.wonderland.rh.utils.LabelUtils;
import io.wonderland.rh.utils.ZxingUtils;
import java.security.Key;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class KeyGeneratorPane extends BorderPane {

  private HToggleBox<RadioButton> keyFormatPane = new HToggleBox<>("Key format : ", 10,s -> new RadioButton(s),
      Map.of("qr-code", () -> setKeyQRCode(), "decimal", () -> setKeyText("Secret key",getSecretKey())));
  private SecretKey secretKey;

  public KeyGeneratorPane() {
    this.setTop(keyFormatPane);
    this.setPadding(new Insets(5,5,5,5));
  }


  public void update(SecretKey secretKey) {
    this.secretKey = secretKey;
    this.setKeyQRCode();
    this.keyFormatPane.selectToggle("decimal");
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
