package io.wonderland.rh.keygen;

import io.wonderland.commons.Arrays;
import io.wonderland.rh.base.common.HToggleBox;
import io.wonderland.rh.utils.LabelUtils;
import io.wonderland.rh.utils.ZxingUtils;
import java.security.Key;
import java.security.KeyPair;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class KeyPairGeneratorPane extends BorderPane {

  public static final String PRIVATE_KEY = "Private key";
  public static final String PUBLIC_KEY = "Public key";
  private Alert alert = new Alert(AlertType.NONE);
  private static final double QR_CODE_TO_VIEW_RATIO = 0.8;
  private HToggleBox<RadioButton> keyFormatPane = new HToggleBox<>("Key format : ", 10, RadioButton::new,
      Map.of("qr-code", () -> setKeyPairQRCode(getKeyPair()), "decimal", () -> setKeyPairText(getKeyPair())));
  private KeyPair keyPair;

  public KeyPairGeneratorPane() {
    this.setTop(keyFormatPane);
    this.setPadding(new Insets(5, 5, 5, 5));
  }


  public void update(KeyPair keyPair) {
    this.keyPair = keyPair;
    this.setKeyPairText(keyPair);
    this.keyFormatPane.selectToggle("decimal");
  }

  private void setKeyPairQRCode(KeyPair keyPair) {
    //set public key
    HBox hBox = new HBox();
    hBox.setSpacing(10);

    try {
      ImageView imageView = ZxingUtils.getImageView(this, keyPair.getPublic());
      VBox vBox = new VBox();
      VBox.setVgrow(imageView, Priority.ALWAYS);
      vBox.getChildren().addAll(LabelUtils.getTitle(PUBLIC_KEY), imageView);
      hBox.getChildren().add(vBox);
    } catch (Exception e) {
      log.error(e.getMessage());
      alert.setAlertType(AlertType.ERROR);
      alert.show();
      alert.setHeaderText(e.getMessage());
    }

    try {
      ImageView imageView = ZxingUtils.getImageView(this, keyPair.getPrivate());
      VBox vBox = new VBox();
      VBox.setVgrow(imageView, Priority.ALWAYS);
      vBox.getChildren().addAll(LabelUtils.getTitle(PRIVATE_KEY), imageView);
      hBox.getChildren().add(vBox);
    } catch (Exception e) {
      log.error(e.getMessage());
      alert.setAlertType(AlertType.ERROR);
      alert.show();
      alert.setHeaderText(e.getMessage());
    }
    this.setCenter(hBox);

  }

  private void setKeyPairText(KeyPair keyPair) {
    VBox vBox = new VBox(getKeyBox(PUBLIC_KEY, keyPair.getPublic()), getKeyBox(PRIVATE_KEY, keyPair.getPrivate()));
    vBox.setSpacing(10);
    this.setCenter(vBox);
  }

  private HBox getKeyBox(String text, Key key) {
    HBox hBox = new HBox();
    TextField textField = new TextField();
    textField.setText(Arrays.getStringValueOf(key.getEncoded(), ','));
    Label label = LabelUtils.getTitle(text);
    HBox.setHgrow(textField, Priority.ALWAYS);
    hBox.getChildren().addAll(label, textField);
    hBox.setSpacing(10);
    return hBox;
  }

}
