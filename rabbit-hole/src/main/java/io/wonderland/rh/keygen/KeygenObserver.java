package io.wonderland.rh.keygen;

import io.wonderland.rh.base.AbstractObserver;
import io.wonderland.rh.base.common.HToggleBox;
import io.wonderland.rh.utils.LabelUtils;
import java.security.Key;
import java.security.KeyPair;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeygenObserver extends AbstractObserver<Object> {

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
    this.vBox.getChildren().addAll(LabelUtils.getTitle("Key type : none."),
        LabelUtils.getTitle("Encryption key : none."));
  }

  private void buildSecretKey() {
    this.vBox.getChildren().addAll(LabelUtils.getTitle("Key type : secret key."),
        LabelUtils.getTitle("Encryption key : secret key."));
  }

  private void buildKeyPair() {
    HToggleBox<RadioButton> encryptionKeyTypePane = new HToggleBox<>("Encryption key : ", 10, RadioButton::new,
        Map.of("Private key", () -> setPublicKeySelected(false), "Public key", () -> setPublicKeySelected(true)));
    this.vBox.getChildren().addAll(LabelUtils.getTitle("Key type : key pair."), encryptionKeyTypePane);
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

  public boolean isKeyUpdated(){
    return isUpdated() && optionalKey.isPresent();
  }
  public Optional<Object> getKey(){
    return optionalKey;
  }
}
