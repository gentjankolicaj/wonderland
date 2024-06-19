package io.wonderland.rh.base.fx;


import io.wonderland.rh.base.barcode.Barcodes;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.codec.CodecAlgs;
import io.wonderland.rh.base.observer.TypeObserver;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.controlsfx.control.SearchableComboBox;

@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class CodecDropdown extends HBox {

  public static final String BASE_10_BLANK = "Base10 ' '";
  public static final String TITLE = "Numeral base : ";
  public static final String CODEC_ALG_SELECTED = "Codec alg selected: {}";
  private final SearchableComboBox<CodecDropdownItem> comboBox = new SearchableComboBox<>();
  private final CodecDropdownItem[] elements;

  public CodecDropdown(String title, String initialSelect, List<CodecDropdownItem> elements) {
    Objects.requireNonNull(elements);
    this.elements = elements.toArray(CodecDropdownItem[]::new);
    this.build(title, initialSelect);
  }

  public static List<CodecAlg> getAllCodecAlgs() {
    List<CodecAlg<byte[], String, String, byte[]>> implCodecAlgs = CodecAlgs.getImplementedCodecAlgs();
    List<CodecAlg<byte[], String, String, byte[]>> runtimeCodecAlgs = CodecAlgs.getRuntimeCodecAlgs();
    return Stream.of(implCodecAlgs, runtimeCodecAlgs)
        .flatMap(List::stream)
        .collect(
            Collectors.toMap(CodecAlg::getName, Function.identity(), (CodecAlg x, CodecAlg y) -> {
              if (x.getName().equals(y.getName())) {
                return x;
              } else {
                return y;
              }
            })).values().stream().sorted(Comparator.comparing(CodecAlg::getRadix))
        .collect(Collectors.toList());
  }


  private static List<CodecDropdownItem> getAllCodecDropdownItems(
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    List<CodecAlg> codecAlgs = getAllCodecAlgs();
    return codecAlgs.stream().map(codecAlg -> new CodecDropdownItem(codecAlg, func))
        .collect(Collectors.toList());
  }

  private static List<CodecDropdownItem> getAllCodecAndBarcodeDropdownItems(
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    List<CodecAlg> codecAlgs = getAllCodecAlgs();
    codecAlgs.addAll(Barcodes.getImplementedBarcodeAlgs());
    return codecAlgs.stream().map(codecAlg -> new CodecDropdownItem(codecAlg, func))
        .collect(Collectors.toList());
  }

  private static List<CodecDropdownItem> getAllBarcodeDropdownItems(
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    List<CodecAlg> codecAlgs = Barcodes.getImplementedBarcodeAlgs();
    return codecAlgs.stream().map(codecAlg -> new CodecDropdownItem(codecAlg, func))
        .collect(Collectors.toList());
  }


  public static CodecDropdown createCodec(
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    return new CodecDropdown(TITLE, BASE_10_BLANK, getAllCodecDropdownItems(func));
  }

  public static CodecDropdown createCodecWithBarcode(
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    return new CodecDropdown(TITLE, BASE_10_BLANK, getAllCodecAndBarcodeDropdownItems(func));
  }

  public static CodecDropdown createBarcode(
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    return new CodecDropdown(TITLE, BASE_10_BLANK, getAllBarcodeDropdownItems(func));
  }

  /**
   * Function applied when numeral base is changed at dropdown.
   *
   * @return functional interface impl
   */
  public static Function<CodecAlg<byte[], String, String, byte[]>, Void> defaultFunc(
      TypeObserver<byte[]> typeObserver,
      CodecTextArea codecTextArea) {
    return codecAlg -> {
      if (typeObserver.getValue().isPresent()) {
        byte[] value = typeObserver.getValue().get();
        if (ArrayUtils.isNotEmpty(value)) {

          //encode ciphertext bytes according to current codec
          String encoded = codecAlg.encode().apply(value);

          //set codec alg with which string was encoded
          codecTextArea.setCodecAlg(codecAlg);

          //set new text
          codecTextArea.clear();
          codecTextArea.setText(encoded);
        }
      }
      return null;
    };
  }

  private void build(String title, String initialSelect) {
    this.comboBox.setConverter(new CodecDropdownItemConverter());
    this.comboBox.setItems(FXCollections.observableArrayList(elements));
    this.comboBox.setEditable(false);
    this.comboBox.setPromptText(title);
    this.comboBox.valueProperty().addListener(getValueChangeListener());
    if (initialSelect != null) {
      this.comboBox.getSelectionModel().select(getItemIndex(initialSelect));
    }
    this.getChildren().addAll(comboBox);
    this.setSpacing(5);
  }

  private ChangeListener<CodecDropdownItem> getValueChangeListener() {
    return (options, oldValue, newValue) -> {
      //for each dropdown element change, get codec algorithm and apply ciphertext encoding
      if (Objects.nonNull(newValue) && Objects.nonNull(newValue.getCodecAlg())) {
        CodecAlg<byte[], String, String, byte[]> codecAlg = newValue.getCodecAlg();
        log.info(CODEC_ALG_SELECTED, codecAlg.getName());
        newValue.getFunc().apply(codecAlg);
      }
    };
  }

  private int getItemIndex(String key) {
    for (int i = 0; i < elements.length; i++) {
      CodecDropdownItem element = elements[i];
      if (key.equals(element.getKey())) {
        return i;
      }
    }
    return 0;
  }

  public CodecDropdownItem getSelectedDropdownItem() {
    return this.comboBox.getSelectionModel().getSelectedItem();
  }

  public void applyFunc() {
    CodecDropdownItem element = comboBox.getSelectionModel().getSelectedItem();
    if (Objects.nonNull(element) && Objects.nonNull(element.getCodecAlg())) {
      CodecAlg<byte[], String, String, byte[]> codecAlg = element.getCodecAlg();
      log.info("Initial applyFunc," + CODEC_ALG_SELECTED, codecAlg.getName());
      element.getFunc().apply(codecAlg);
    }
  }

  private class CodecDropdownItemConverter extends StringConverter<CodecDropdownItem> {

    @Override
    public String toString(CodecDropdownItem element) {
      return element == null ? null : element.getKey();
    }

    @Override
    public CodecDropdownItem fromString(String s) {
      for (CodecDropdownItem element : elements) {
        if (element != null && element.getKey().equals(s)) {
          return element;
        }
      }
      return null;
    }
  }


}
