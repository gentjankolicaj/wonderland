package io.wonderland.rh.cipher;

import io.wonderland.commons.Arrays;
import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.base.common.Dropdown;
import io.wonderland.rh.base.common.DropdownElement;
import io.wonderland.rh.utils.BaseUtils;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DropdownHelper {

  private DropdownHelper() {
  }

  public static Dropdown<String, byte[], TextArea> getEncodingDropdown(TextArea textArea, TypeObserver<byte[]> typeObserver) {
    DropdownElement<String, byte[], TextArea>[] dropdownElements = ArrayUtils.addAll(getBase64(), getBase32());
    dropdownElements = ArrayUtils.addAll(dropdownElements, getHex());
    dropdownElements = ArrayUtils.addAll(dropdownElements, getDecimal());
    dropdownElements = ArrayUtils.addAll(dropdownElements, getOctal());
    dropdownElements = ArrayUtils.addAll(dropdownElements, getBinary());

    return new Dropdown<>("Encoding scheme : ", null,textArea, typeObserver, dropdownElements);
  }

  private static DropdownElement<String, byte[], TextArea>[] getBase64() {
    return Arrays.asArray(new DropdownElement<>("Base64 (UC)", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(Base64.getEncoder().encodeToString(observer.getValue().get()).toUpperCase());
          }
        }),
        new DropdownElement<>("Base64 (LC)", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(Base64.getEncoder().encodeToString(observer.getValue().get()).toLowerCase());
          }
        }));
  }

  private static DropdownElement<String, byte[], TextArea>[] getBase32() {
    return Arrays.asArray(new DropdownElement<>("Base32 (UC)", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase32(false, observer.getValue().get()));
          }
        }),
        new DropdownElement<>("Base32 (LC)", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase32(true, observer.getValue().get()));
          }
        }));
  }

  private static DropdownElement<String, byte[], TextArea>[] getHex() {
    return Arrays.asArray(new DropdownElement<>("Base16 (UC)", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase16(false, observer.getValue().get()));
          }
        }),
        new DropdownElement<>("Base16 (LC)", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase16(true, observer.getValue().get()));
          }
        }));
  }

  private static DropdownElement<String, byte[], TextArea>[] getDecimal() {
    return Arrays.asArray(new DropdownElement<>("Base10 ','", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase10(observer.getValue().get(), ','));
          }
        }),
        new DropdownElement<>("Base10 ' '", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase10(observer.getValue().get(), ' '));
          }
        }));
  }

  private static DropdownElement<String, byte[], TextArea>[] getOctal() {
    return Arrays.asArray(new DropdownElement<>("Base8 ','", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase8(observer.getValue().get(), ','));
          }
        }),
        new DropdownElement<>("Base8 ' '", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase8(observer.getValue().get(), ' '));
          }
        }));
  }

  private static DropdownElement<String, byte[], TextArea>[] getBinary() {
    return Arrays.asArray(new DropdownElement<>("Base2 ','", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase2(observer.getValue().get(), ','));
          }
        }),
        new DropdownElement<>("Base2 ' '", (observer, textarea) -> {
          if (observer.getValue().isPresent()) {
            textarea.clear();
            textarea.setText(BaseUtils.getBase2(observer.getValue().get(), ' '));
          }
        }));
  }

  public static Dropdown<String, byte[], TextArea> getCharsetDropdown(TextArea textArea, TypeObserver<byte[]> typeObserver) {
    return new Dropdown<>("Charset : ", StandardCharsets.UTF_8.name(), textArea, typeObserver, getCharsetsElements());
  }

  private static List<DropdownElement<String, byte[], TextArea>> getCharsetsElements(){
    return Charset.availableCharsets().entrySet().stream().map(e->
      new DropdownElement<String,byte[],TextArea>(e.getKey(), (observer, textarea) -> {
        if(StringUtils.isNotEmpty(textarea.getText())) {
          String actual=textarea.getText();
          String newText=new String(actual.getBytes(),e.getValue());
          textarea.clear();
          textarea.setText(newText);
          observer.update(newText.getBytes(e.getValue()));
        }
      })
    ).collect(Collectors.toList());

  }





}
