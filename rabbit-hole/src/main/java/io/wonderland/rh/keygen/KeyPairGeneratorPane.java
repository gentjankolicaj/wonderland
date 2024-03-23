package io.wonderland.rh.keygen;

import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.base.dropdown.Dropdown;
import io.wonderland.rh.base.dropdown.DropdownHelper;
import io.wonderland.rh.utils.GuiUtils;
import io.wonderland.rh.utils.ZxingUtils;
import java.security.KeyPair;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
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
  private final TypeObserver<KeyPair> keyPairTypeObserver = new TypeObserver<>();
  private Dropdown<String, KeyPair, KeyPairGeneratorPane> dropdown = DropdownHelper.getKeyPairGeneratorPaneDropdown(
      keyPairTypeObserver, this);
  private KeyPair keyPair;

  public KeyPairGeneratorPane() {
    this.setTop(dropdown);
    this.setPadding(new Insets(5, 5, 5, 5));
  }


  public void update(KeyPair keyPair) {
    this.keyPair = keyPair;
    this.keyPairTypeObserver.update(keyPair);
    this.dropdown.getSelectedDropdownElement().runConsumer(keyPairTypeObserver, this);
  }

  public static HBox getKeyPairQRCode(BorderPane pane, KeyPair keyPair) {
    HBox hBox = new HBox();
    hBox.setSpacing(10);
    try {
      ImageView imageView = ZxingUtils.getImageView(pane, keyPair.getPublic());
      VBox vBox = new VBox();
      javafx.scene.layout.VBox.setVgrow(imageView, Priority.ALWAYS);
      vBox.getChildren().addAll(GuiUtils.getTitle(PUBLIC_KEY), imageView);
      hBox.getChildren().add(vBox);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    try {
      ImageView imageView = ZxingUtils.getImageView(pane, keyPair.getPrivate());
      VBox vBox = new VBox();
      javafx.scene.layout.VBox.setVgrow(imageView, Priority.ALWAYS);
      vBox.getChildren().addAll(GuiUtils.getTitle(PRIVATE_KEY), imageView);
      hBox.getChildren().add(vBox);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return hBox;
  }

  public static VBox getKeyPairTextBox(String publicKeyFormattedText, String privateKeyFormattedText) {
    VBox vBox = new VBox(getKeyTextBox(PUBLIC_KEY, publicKeyFormattedText),
        getKeyTextBox(PRIVATE_KEY, privateKeyFormattedText));
    vBox.setSpacing(10);
    return vBox;
  }

  public static HBox getKeyTextBox(String title, String formattedText) {
    HBox hBox = new HBox();
    TextField textField = new TextField();
    textField.setText(formattedText);
    Label label = GuiUtils.getTitle(title);
    HBox.setHgrow(textField, Priority.ALWAYS);
    hBox.getChildren().addAll(label, textField);
    hBox.setSpacing(10);
    return hBox;
  }


}
