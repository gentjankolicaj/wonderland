package io.wonderland.rh.keygen;

import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.base.common.Dropdown;
import io.wonderland.rh.base.common.DropdownHelper;
import io.wonderland.rh.utils.GuiUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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

  private final TypeObserver<SecretKey> secretKeyObserver = new TypeObserver<>();
  private Dropdown<String, SecretKey, KeyGeneratorPane> dropdown = DropdownHelper.getKeyGeneratorPaneDropdown(
      secretKeyObserver, this);
  private SecretKey secretKey;

  public KeyGeneratorPane() {
    this.setTop(dropdown);
    this.setPadding(new Insets(5,5,5,5));
  }


  public void update(SecretKey secretKey) {
    this.secretKey = secretKey;
    this.secretKeyObserver.update(secretKey);
    this.dropdown.getSelectedDropdownElement().runConsumer(secretKeyObserver, this);
  }

  public static HBox getKeyTextBox(String formattedText) {
    HBox hBox=new HBox();
    TextField textField=new TextField();
    textField.setText(formattedText);
    Label label = GuiUtils.getTitle("Secret key");
    HBox.setHgrow(textField, Priority.ALWAYS);
    hBox.getChildren().addAll(label,textField);
    hBox.setSpacing(10);
    return hBox;
  }


}
