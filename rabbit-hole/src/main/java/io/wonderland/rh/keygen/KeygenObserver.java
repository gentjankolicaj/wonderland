package io.wonderland.rh.keygen;

import io.wonderland.rh.base.KeyObserver;
import io.wonderland.rh.base.pane.HToggleBox;
import io.wonderland.rh.utils.GuiUtils;
import java.security.Key;
import java.security.KeyPair;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeygenObserver extends KeyObserver<Object> {

  @Getter
  private VBox vBox =new VBox();
  private boolean isPublicKeySelected;
  private Optional<Object> optionalKey=Optional.empty();
  private boolean updated=true;

  public VBox getContainerBox(){
    buildEmpty();
    return vBox;
  }

  private void clearContainerBox(){
    this.vBox.getChildren().removeAll(this.vBox.getChildren());
  }

  private void buildEmpty() {
    this.vBox.getChildren().addAll(new HBox(GuiUtils.getTitle("Key type : "),new Label("none")),
        new HBox(GuiUtils.getTitle("Encryption key : "),new Label("none")));
  }

  private void buildSecretKey() {
    this.vBox.getChildren().addAll(new HBox(GuiUtils.getTitle("Key type : "),new Label("Secret key")),
        new HBox(GuiUtils.getTitle("Encryption key : "),new Label("Secret key")));
  }

  private void buildKeyPair() {
    HToggleBox<RadioButton> encryptionKeyTypePane = new HToggleBox<>("Encryption key : ", 10, RadioButton::new,
        Map.of("Private key", () -> setPublicKeySelected(false), "Public key", () -> setPublicKeySelected(true)));
    encryptionKeyTypePane.selectToggle("Public key");
    this.vBox.getChildren().addAll(new HBox(GuiUtils.getTitle("Key type : "),new Label("Key pair")),encryptionKeyTypePane);
  }

  @Override
  public void update(Object notification) {
    if(Objects.nonNull(notification)){
      this.updated=true;
      this.clearContainerBox();
      if (notification instanceof Key) {
        buildSecretKey();
        this.optionalKey=Optional.of(notification);
      } else if (notification instanceof KeyPair) {
        buildKeyPair();
        this.optionalKey=Optional.of(notification);
      } else {
        buildEmpty();
      }
    }
  }

}
