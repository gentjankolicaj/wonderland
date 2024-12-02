package io.wonderland.rh.export;

import io.atlassian.fugue.Either;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.HToggleBox;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class KeyExportDialog extends Dialog<Either<KeyBox, KeystoreBox>> {

  private final VBox container = new VBox();
  private Either<KeyBox, KeystoreBox> either;

  private final HToggleBox<RadioButton> keyExportPane = new HToggleBox<>("Export type :", 10,
      RadioButton::new,
      Map.of("raw key", this::setRawKeyBox, "keystore", this::setKeystoreBox));

  public KeyExportDialog() {
    build();

  }

  private void build() {
    this.container.getChildren().add(keyExportPane);
    this.container.setSpacing(GlobalConstants.SPACING);
    getDialogPane().getButtonTypes().addAll(getButtons());
    resize();
    getDialogPane().setContent(container);
    setResultConverter(new DialogResultConverter());
  }

  private void resize() {
    getDialogPane().setMinSize(GlobalConstants.KEY_EXPORT_DIALOG_SIZE[0],
        GlobalConstants.KEY_EXPORT_DIALOG_SIZE[1]);
  }

  private void setKeystoreBox() {
    if (this.container.getChildren().size() > 1) {
      this.container.getChildren().remove(1);
    }
    KeystoreBox keystoreBox = new KeystoreBox();
    this.either = Either.right(keystoreBox);
    this.container.getChildren().add(1, keystoreBox);
  }

  private void setRawKeyBox() {
    if (this.container.getChildren().size() > 1) {
      this.container.getChildren().remove(1);
    }
    KeyBox keyBox = new KeyBox();
    this.either = Either.left(keyBox);
    this.container.getChildren().add(1, keyBox);
  }

  private List<ButtonType> getButtons() {
    ButtonType exportBtn = new ButtonType("Export", ButtonData.OK_DONE);
    return List.of(exportBtn, ButtonType.CANCEL);
  }

  private Either getEither() {
    return this.either;
  }

  private class DialogResultConverter implements Callback<ButtonType, Either<KeyBox, KeystoreBox>> {

    @Override
    public Either<KeyBox, KeystoreBox> call(ButtonType buttonType) {
      if (!buttonType.getButtonData().isCancelButton()) {
        return getEither();
      } else {
        return Either.left(null);
      }
    }
  }


}
