package io.wonderland.rh.cipher.key;

import java.security.Key;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

@Slf4j
public  class TextFieldKeyPane<T extends Key> extends AbstractKeyPane<T> {

  @Getter
  protected String keyLabel;
  protected VBox vBox = new VBox();
  protected Consumer<Object> cipherStateConsumer;
  protected Map<String, TextField> textFieldMap;


  protected TextFieldKeyPane(String keyLabel, Consumer<Object> cipherStateConsumer,
      Map<String, TextField> textFieldMap) {
    Objects.requireNonNull(keyLabel, "Key label can't be null.");
    Objects.requireNonNull(cipherStateConsumer, "Cipher state consumer can't be null.");
    if (MapUtils.isEmpty(textFieldMap)) {
      throw new IllegalArgumentException("Text field map can't be null.");
    }
    this.keyLabel = keyLabel;
    this.cipherStateConsumer = cipherStateConsumer;
    this.textFieldMap = textFieldMap;
    this.setup();
  }

  private void setup() {
    //key scroll pane
    if (Objects.nonNull(this.textFieldMap)) {
      this.textFieldMap.forEach((key, value) -> {
        //Add label & text field to vertical box
        HBox hBox = new HBox(getLabel(key + " : "), value);
        HBox.setHgrow(value, Priority.ALWAYS);
        hBox.setFillHeight(true);
        this.vBox.getChildren().add(hBox);
        if (Objects.nonNull(cipherStateConsumer)) {
          //set stale state to cipher
          cipherStateConsumer.accept(value);
        }
      });
    }

    this.vBox.setSpacing(13);
    this.vBox.setFillWidth(true);

    this.setTop(this.getLabel(this.keyLabel));
    this.setCenter(this.vBox);
  }

  protected Label getLabel(String value) {
    Label label = new Label(value);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }


  public T getCipherKey() {
    throw new UnsupportedOperationException("Unimplemented.");
  }


 public Key getKey() {
    throw new UnsupportedOperationException("Unimplemented.");
  }

  @Override
  public boolean removeKey() {
    return false;
  }
}
