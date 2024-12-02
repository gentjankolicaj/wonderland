package io.wonderland.rh.base.observer;

import static io.wonderland.rh.GlobalConstants.PRIVATE_KEY;
import static io.wonderland.rh.GlobalConstants.PUBLIC_KEY;
import static io.wonderland.rh.GlobalConstants.SECRET_KEY;

import io.atlassian.fugue.Either;
import io.wonderland.rh.base.fx.HToggleBox;
import io.wonderland.rh.utils.GuiUtils;
import java.security.KeyPair;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KeygenObserver extends EitherKeyObserver {

  public static final String KEY_TYPE = "Key type : ";
  public static final String ENCRYPTION_KEY = "Encryption key : ";
  public static final String NONE = "none";

  private final VBox vBox = new VBox();
  private boolean isPublicKeySelected;

  public VBox getContainerBox() {
    buildEmpty();
    return vBox;
  }

  private void clearContainerBox() {
    this.vBox.getChildren().removeAll(this.vBox.getChildren());
  }

  private void buildEmpty() {
    this.vBox.getChildren().addAll(new HBox(GuiUtils.getTitle(KEY_TYPE), new Label(NONE)),
        new HBox(GuiUtils.getTitle(ENCRYPTION_KEY), new Label(NONE)));
  }

  private void buildSecretKey() {
    this.vBox.getChildren().addAll(new HBox(GuiUtils.getTitle(KEY_TYPE), new Label(SECRET_KEY)),
        new HBox(GuiUtils.getTitle(ENCRYPTION_KEY), new Label(SECRET_KEY)));
  }

  private void buildKeyPair() {
    HToggleBox<RadioButton> encryptionKeyTypePane = new HToggleBox<>(ENCRYPTION_KEY, 10,
        RadioButton::new,
        Map.of(PRIVATE_KEY, () -> setPublicKeySelected(false), PUBLIC_KEY,
            () -> setPublicKeySelected(true)));
    encryptionKeyTypePane.selectToggle(PUBLIC_KEY);
    this.vBox.getChildren()
        .addAll(new HBox(GuiUtils.getTitle(KEY_TYPE), new Label("Key pair")),
            encryptionKeyTypePane);
  }

  @Override
  public void update(Either<SecretKey, KeyPair> notification) {
    if (Objects.nonNull(notification)) {
      this.updated = true;
      this.optionalKey = Optional.of(notification);
      this.clearContainerBox();
      if (notification.isLeft()) {
        buildSecretKey();
      } else if (notification.isRight()) {
        buildKeyPair();
        this.optionalKey = Optional.of(notification);
      } else {
        buildEmpty();
      }
    }
  }

}
