package io.wonderland.rh.cipher.key;

import io.wonderland.alice.crypto.cipher.key.ByteArrayKey;
import java.security.Key;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public  class TextAreaKeyPane<T extends Key> extends AbstractKeyPane<T> {

  @Getter
  protected String keyLabel;
  protected TextArea textArea = new TextArea();
  protected Consumer cipherStateConsumer;

  public TextAreaKeyPane(String keyLabel, Consumer cipherStateConsumer) {
    this.keyLabel = keyLabel;
    this.cipherStateConsumer = cipherStateConsumer;
    this.setup();
  }

  private void setup() {
    //key scroll pane

    //add text listener
    this.textArea.textProperty().addListener((obs, old, niu) -> {
      if (Objects.nonNull(cipherStateConsumer)) {
        //set stale state to cipher
        cipherStateConsumer.accept(obs);
      } else {
        log.info("Key state changed.");
      }
    });
    this.setTop(this.getKeyLbl(this.keyLabel));
    this.setCenter(textArea);
  }

  protected Label getKeyLbl(String keyLabel) {
    Label label = new Label(keyLabel);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }


  public T getCipherKey() {
    throw new UnsupportedOperationException("Unimplemented.");
  }


 public Key getKey() {
    return new ByteArrayKey(textArea.getText().getBytes());
  }


 public boolean removeKey() {
    this.textArea.clear();
    return true;
  }

}
