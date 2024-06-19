package io.wonderland.rh.key.secret;

import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.utils.GuiUtils;
import java.security.Key;
import java.util.Map;
import java.util.Objects;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

@Slf4j
public class TextFieldKeyPane<T extends Key> extends AbstractKeyPane<T> {

  protected VBox vBox = new VBox();

  @Getter
  protected String keyAlgorithm;
  protected EitherKeyObserver eitherKeyObserver;
  protected Map<String, TextField> textFieldMap;


  protected TextFieldKeyPane(String keyAlgorithm, EitherKeyObserver eitherKeyObserver,
      Map<String, TextField> textFieldMap) {
    Objects.requireNonNull(keyAlgorithm, "Key label can't be null.");

    if (MapUtils.isEmpty(textFieldMap)) {
      throw new IllegalArgumentException("Text field map can't be null.");
    }
    this.keyAlgorithm = keyAlgorithm;
    this.eitherKeyObserver = eitherKeyObserver;
    this.textFieldMap = textFieldMap;
    this.setup();
  }

  private void setup() {
    //key scroll pane
    if (Objects.nonNull(this.textFieldMap)) {
      this.textFieldMap.forEach((key, value) -> {
        //Add label & text field to vertical box
        HBox hBox = new HBox(GuiUtils.getTitle(key + " : "), value);
        HBox.setHgrow(value, Priority.ALWAYS);
        hBox.setFillHeight(true);
        this.vBox.getChildren().add(hBox);
      });
    }

    this.vBox.setSpacing(13);
    this.vBox.setFillWidth(true);

    this.setTop(GuiUtils.getTitle(this.keyAlgorithm));
    this.setCenter(this.vBox);
  }


  public T getCipherKey() {
    throw new UnsupportedOperationException("Unimplemented.");
  }

  public SecretKey getKey() {
    throw new UnsupportedOperationException("Unimplemented.");
  }

  @Override
  public boolean removeKey() {
    return false;
  }
}
