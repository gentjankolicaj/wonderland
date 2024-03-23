package io.wonderland.rh.base.dropdown;

import io.wonderland.commons.Arrays;
import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.keygen.KeyGeneratorPane;
import io.wonderland.rh.keygen.KeyPairGeneratorPane;
import io.wonderland.rh.utils.BaseUtils;
import io.wonderland.rh.utils.ZxingUtils;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DropdownHelper {


  public static final String BASE_10_BLANK = "Base10 ' '";
  public static final String BASE_10_COMMA = "Base10 ','";
  public static final String BASE_64_UC = "Base64 (UC)";
  public static final String BASE_64_LC = "Base64 (LC)";
  public static final String BASE_32_UC = "Base32 (UC)";
  public static final String BASE_32_LC = "Base32 (LC)";
  public static final String BASE_16_UC = "Base16 (UC)";
  public static final String BASE_16_LC = "Base16 (LC)";
  public static final String BASE_8_COMMA = "Base8 ','";
  public static final String BASE_8_BLANK = "Base8 ' '";
  public static final String BASE_2_COMMA = "Base2 ','";
  public static final String BASE_2_BLANK = "Base2 ' '";

  private DropdownHelper() {
  }

  public static Dropdown<String, byte[], TextArea> getEncodingDropdown(TextArea textArea,
      TypeObserver<byte[]> typeObserver) {
    return new Dropdown<>("Encoding scheme : ", null, textArea, typeObserver,
        ArrayUtils.addAll(getUpperBase(), getLowerBase()));
  }

  private static DropdownElement<String, byte[], TextArea>[] getUpperBase() {
    return Arrays.asArray(new DropdownElement<>(BASE_64_UC, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(Base64.getEncoder().encodeToString(observer.getValue().get()).toUpperCase());
          }
        }),
        new DropdownElement<>(BASE_64_LC, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(Base64.getEncoder().encodeToString(observer.getValue().get()).toLowerCase());
          }
        }), new DropdownElement<>(BASE_32_UC, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase32(false, observer.getValue().get()));
          }
        }),
        new DropdownElement<>(BASE_32_LC, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase32(true, observer.getValue().get()));
          }
        }), new DropdownElement<>(BASE_16_UC, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase16(false, observer.getValue().get()));
          }
        }),
        new DropdownElement<>(BASE_16_LC, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase16(true, observer.getValue().get()));
          }
        }));
  }

  private static DropdownElement<String, byte[], TextArea>[] getLowerBase() {
    return Arrays.asArray(new DropdownElement<>(BASE_10_COMMA, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase10(observer.getValue().get(), ','));
          }
        }),
        new DropdownElement<>(BASE_10_BLANK, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase10(observer.getValue().get(), ' '));
          }
        }), new DropdownElement<>(BASE_8_COMMA, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase8(observer.getValue().get(), ','));
          }
        }),
        new DropdownElement<>(BASE_8_BLANK, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase8(observer.getValue().get(), ' '));
          }
        }), new DropdownElement<>(BASE_2_COMMA, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase2(observer.getValue().get(), ','));
          }
        }),
        new DropdownElement<>(BASE_2_BLANK, (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase2(observer.getValue().get(), ' '));
          }
        }));
  }

  public static Dropdown<String, byte[], TextArea> getCharsetDropdown(TextArea textArea,
      TypeObserver<byte[]> typeObserver) {
    return new Dropdown<>("Charset : ", StandardCharsets.UTF_8.name(), textArea, typeObserver, getCharsetsElements());
  }

  private static List<DropdownElement<String, byte[], TextArea>> getCharsetsElements() {
    return Charset.availableCharsets().entrySet().stream().map(e ->
        new DropdownElement<String, byte[], TextArea>(e.getKey(), (observer, textarea) -> {
          if (StringUtils.isNotEmpty(textarea.getText())) {
            String actual = textarea.getText();
            String newText = new String(actual.getBytes(), e.getValue());
            textarea.clear();
            textarea.setText(newText);
            observer.update(newText.getBytes(e.getValue()));
          }
        })
    ).collect(Collectors.toList());

  }

  public static Dropdown<String, SecretKey, KeyGeneratorPane> getKeyGeneratorPaneDropdown(
      TypeObserver<SecretKey> secretKeyObserver, KeyGeneratorPane parentPane) {
    return new Dropdown<>("Key format : ", BASE_10_BLANK, parentPane, secretKeyObserver, getKeyFormatElements());
  }

  private static DropdownElement<String, SecretKey, KeyGeneratorPane>[] getKeyFormatElements() {
    DropdownElement<String, SecretKey, KeyGeneratorPane>[] arr = ArrayUtils.addAll(getUpperBaseKeyFormats(),
        getLowerBaseKeyFormats());
    return ArrayUtils.addAll(arr, getCodeKeyFormats());
  }

  private static DropdownElement<String, SecretKey, KeyGeneratorPane>[] getUpperBaseKeyFormats() {
    return Arrays.asArray(new DropdownElement<>(BASE_64_UC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(
                Base64.getEncoder().encodeToString(observer.getValue().get().getEncoded()).toUpperCase());
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_64_LC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(
                Base64.getEncoder().encodeToString(observer.getValue().get().getEncoded()).toLowerCase());
            pane.setCenter(box);
          }
        }), new DropdownElement<>(BASE_32_UC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(
                BaseUtils.getBase32(false, observer.getValue().get().getEncoded()));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_32_LC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(
                BaseUtils.getBase32(true, observer.getValue().get().getEncoded()));
            pane.setCenter(box);
          }
        }),

        new DropdownElement<>(BASE_16_UC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(
                BaseUtils.getBase16(false, observer.getValue().get().getEncoded()));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_16_LC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(
                BaseUtils.getBase16(true, observer.getValue().get().getEncoded()));
            pane.setCenter(box);
          }
        }));
  }

  private static DropdownElement<String, SecretKey, KeyGeneratorPane>[] getLowerBaseKeyFormats() {
    return Arrays.asArray(
        new DropdownElement<>(BASE_10_COMMA, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(BaseUtils.getBase10(observer.getValue().get().getEncoded(), ','));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_10_BLANK, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(BaseUtils.getBase10(observer.getValue().get().getEncoded(), ' '));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_8_COMMA, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(BaseUtils.getBase8(observer.getValue().get().getEncoded(), ','));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_8_BLANK, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(BaseUtils.getBase8(observer.getValue().get().getEncoded(), ' '));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_2_COMMA, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(BaseUtils.getBase2(observer.getValue().get().getEncoded(), ','));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_2_BLANK, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            HBox box = KeyGeneratorPane.getKeyTextBox(BaseUtils.getBase2(observer.getValue().get().getEncoded(), ' '));
            pane.setCenter(box);
          }
        }));
  }

  private static DropdownElement<String, SecretKey, KeyGeneratorPane>[] getCodeKeyFormats() {
    return Arrays.asArray(new DropdownElement<>("QRCode", (observer, pane) -> {
      if (observer.getValue().isPresent()) {
        try {
          ImageView imageView = ZxingUtils.getImageView(pane, observer.getValue().get());
          pane.setCenter(imageView);
        } catch (Exception e) {
          log.error(e.getMessage());
        }
      }
    }));
  }

  public static Dropdown<String, KeyPair, KeyPairGeneratorPane> getKeyPairGeneratorPaneDropdown(
      TypeObserver<KeyPair> keyPairObserver, KeyPairGeneratorPane parentPane) {
    return new Dropdown<>("Key format : ", BASE_10_BLANK, parentPane, keyPairObserver, getKeyPairFormatElements());
  }

  private static DropdownElement<String, KeyPair, KeyPairGeneratorPane>[] getKeyPairFormatElements() {
    DropdownElement<String, KeyPair, KeyPairGeneratorPane>[] arr = ArrayUtils.addAll(getUpperBaseKeyPairFormats(),
        getLowerBaseKeyPairFormats());
    return ArrayUtils.addAll(arr, getCodeKeyPairFormats());
  }

  private static DropdownElement<String, KeyPair, KeyPairGeneratorPane>[] getUpperBaseKeyPairFormats() {
    return Arrays.asArray(new DropdownElement<>(BASE_64_UC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()).toUpperCase(),
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()).toUpperCase()
            );
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_64_LC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()).toLowerCase(),
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()).toLowerCase()
            );
            pane.setCenter(box);
          }
        }), new DropdownElement<>(BASE_32_UC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                BaseUtils.getBase32(false, keyPair.getPublic().getEncoded()),
                BaseUtils.getBase32(false, keyPair.getPrivate().getEncoded())
            );
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_32_LC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                BaseUtils.getBase32(true, keyPair.getPublic().getEncoded()),
                BaseUtils.getBase32(true, keyPair.getPrivate().getEncoded())
            );
            pane.setCenter(box);
          }
        }),

        new DropdownElement<>(BASE_16_UC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                BaseUtils.getBase16(false, keyPair.getPublic().getEncoded()),
                BaseUtils.getBase16(false, keyPair.getPrivate().getEncoded())
            );
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_16_LC, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                BaseUtils.getBase16(true, keyPair.getPublic().getEncoded()),
                BaseUtils.getBase16(true, keyPair.getPrivate().getEncoded())
            );
            pane.setCenter(box);
          }
        }));
  }

  private static DropdownElement<String, KeyPair, KeyPairGeneratorPane>[] getLowerBaseKeyPairFormats() {
    return Arrays.asArray(
        new DropdownElement<>(BASE_10_COMMA, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                BaseUtils.getBase10(keyPair.getPublic().getEncoded(), ','),
                BaseUtils.getBase10(keyPair.getPrivate().getEncoded(), ','));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_10_BLANK, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(
                BaseUtils.getBase10(keyPair.getPublic().getEncoded(), ' '),
                BaseUtils.getBase10(keyPair.getPrivate().getEncoded(), ' '));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_8_COMMA, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(BaseUtils.getBase8(keyPair.getPublic().getEncoded(), ','),
                BaseUtils.getBase8(keyPair.getPrivate().getEncoded(), ','));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_8_BLANK, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(BaseUtils.getBase8(keyPair.getPublic().getEncoded(), ' '),
                BaseUtils.getBase8(keyPair.getPrivate().getEncoded(), ' '));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_2_COMMA, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(BaseUtils.getBase2(keyPair.getPublic().getEncoded(), ','),
                BaseUtils.getBase2(keyPair.getPrivate().getEncoded(), ','));
            pane.setCenter(box);
          }
        }),
        new DropdownElement<>(BASE_2_BLANK, (observer, pane) -> {
          if (observer.getValue().isPresent()) {
            KeyPair keyPair = observer.getValue().get();
            VBox box = KeyPairGeneratorPane.getKeyPairTextBox(BaseUtils.getBase2(keyPair.getPublic().getEncoded(), ' '),
                BaseUtils.getBase2(keyPair.getPrivate().getEncoded(), ' '));
            pane.setCenter(box);
          }
        }));
  }

  private static DropdownElement<String, KeyPair, KeyPairGeneratorPane>[] getCodeKeyPairFormats() {
    return Arrays.asArray(new DropdownElement<>("QRCode", (observer, pane) -> {
      if (observer.getValue().isPresent()) {
        try {
          HBox hBox = KeyPairGeneratorPane.getKeyPairQRCode(pane, observer.getValue().get());
          pane.setCenter(hBox);
        } catch (Exception e) {
          log.error(e.getMessage());
        }
      }
    }));
  }

}
