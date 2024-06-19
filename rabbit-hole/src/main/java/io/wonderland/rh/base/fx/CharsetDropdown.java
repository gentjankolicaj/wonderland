package io.wonderland.rh.base.fx;


import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.observer.TypeObserver;
import io.wonderland.rh.cipher.mediator.Plaintext;
import io.wonderland.rh.cipher.mediator.PlaintextArea;
import io.wonderland.rh.exception.CodecException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;


@SuppressWarnings("unchecked")
@Slf4j
public class CharsetDropdown<K, T, R> extends HBox {

  private final SearchableComboBox<CharsetDropdownItem<T, R>> comboBox = new SearchableComboBox<>();
  private final String title;
  private final CharsetDropdownItem<T, R>[] elements;


  private CharsetDropdown(String title, K initialSelect, List<CharsetDropdownItem<T, R>> elements) {
    Objects.requireNonNull(elements);
    this.elements = elements.toArray(CharsetDropdownItem[]::new);
    this.title = title;
    this.build(initialSelect);
  }

  private static <T, R> ChangeListener<CharsetDropdownItem<T, R>> getValueChangeListener() {
    return (options, oldValue, newValue) -> {
      if (Objects.nonNull(newValue) && Objects.nonNull(newValue.getFunc())) {
        try {
          log.info("PT-CHARSET: {}", newValue.getInput());
          newValue.func.apply(newValue.getInput());
        } catch (Exception e) {
          throw new CodecException(e);
        }
      }
    };
  }

  private static List<CharsetDropdownItem<Charset, Void>> getCharsetsElements(
      Function<Charset, Void> func) {
    return Charset.availableCharsets().entrySet().stream()
        .map(charset -> new CharsetDropdownItem<>(charset.getKey(), charset.getValue(), func))
        .collect(Collectors.toList());
  }

  public static CharsetDropdown<String, Charset, Void> create(Function<Charset, Void> func) {
    return new CharsetDropdown<>("Charset : ", GlobalConstants.DEFAULT_CHARSET.name(),
        getCharsetsElements(func));
  }

  public static Function<Charset, Void> func(CharsetTextArea charsetTextArea,
      TypeObserver<byte[]> observer) {
    return newCharset -> {
      if (StringUtils.isNotEmpty(charsetTextArea.getText())) {
        String actual = charsetTextArea.getText();
        Charset oldCharset = charsetTextArea.getCharset();
        String text = new String(actual.getBytes(oldCharset), newCharset);
        //set new text & charset
        charsetTextArea.clear();
        charsetTextArea.setText(text);
        charsetTextArea.setCharset(newCharset);
        observer.update(text.getBytes(newCharset));
      }
      return null;
    };
  }

  public static Function<Charset, Void> func(PlaintextArea plaintextArea, Plaintext plaintext) {
    return newCharset -> {
      if (StringUtils.isNotEmpty(plaintextArea.getText())) {
        String actual = plaintextArea.getText();
        Charset oldCharset = plaintextArea.getCharset();
        String text = new String(actual.getBytes(oldCharset), newCharset);

        //set new text & charset
        plaintextArea.clear();
        plaintextArea.setText(text);
        plaintextArea.setCharset(newCharset);

        //set plaintext value
        plaintext.setValue(text.getBytes(newCharset));
      }
      return null;
    };
  }

  private void build(K initialSelect) {
    this.comboBox.setConverter(new CharsetDropdownElementConverter());
    this.comboBox.setItems(FXCollections.observableArrayList(elements));
    this.comboBox.setEditable(false);
    this.comboBox.setPromptText(title);
    this.comboBox.valueProperty().addListener(getValueChangeListener());
    if (initialSelect != null) {
      this.comboBox.getSelectionModel().select(getElementIndex(initialSelect));
    }
    this.getChildren().addAll(comboBox);
    this.setSpacing(5);
  }

  private int getElementIndex(K key) {
    for (int i = 0; i < elements.length; i++) {
      CharsetDropdownItem<T, R> element = elements[i];
      if (key.equals(element.getKey())) {
        return i;
      }
    }
    return 0;
  }

  public CharsetDropdownItem<T, R> getSelectedDropdownElement() {
    return this.comboBox.getSelectionModel().getSelectedItem();
  }

  private class CharsetDropdownElementConverter extends StringConverter<CharsetDropdownItem<T, R>> {

    @Override
    public String toString(CharsetDropdownItem<T, R> element) {
      return element == null ? null : element.getKey();
    }

    @Override
    public CharsetDropdownItem<T, R> fromString(String s) {
      for (CharsetDropdownItem<T, R> element : elements) {
        if (element != null && element.getKey().equals(s)) {
          return element;
        }
      }
      return null;
    }
  }

}
