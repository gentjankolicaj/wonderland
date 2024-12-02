package io.wonderland.rh.cipher.mediator;

import io.wonderland.rh.base.mediator.Component;
import io.wonderland.rh.base.mediator.Mediator;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public class CipherMediator implements Mediator<String> {

  private final Component<String>[] components;
  private Plaintext plaintext;
  private Ciphertext ciphertext;
  private PlaintextArea plaintextArea;
  private CiphertextArea ciphertextArea;

  @SafeVarargs
  public CipherMediator(Component<String>... components) {
    this.components = components;
    this.registerComponents(components);
    this.addListeners();
  }

  @Override
  public Map<String, Component<String>> getComponents() {
    if (ArrayUtils.isEmpty(components)) {
      return Map.of();
    } else {
      return Arrays.stream(components)
          .collect(Collectors.toMap(Component::getKey, Component::getValue));
    }
  }

  private void addListeners() {
    plaintextArea.textProperty().addListener(event -> updatePlaintext());
    ciphertextArea.textProperty().addListener(keyEvent -> updateCiphertext());
  }

  private void updatePlaintext() {
    this.plaintext.setValue(plaintextArea.getValue());
  }

  private void updateCiphertext() {
    this.ciphertext.setValue(ciphertextArea.getValue());
  }


  @SafeVarargs
  public final void registerComponents(Component<String>... components) {
    if (ArrayUtils.isEmpty(components)) {
      throw new IllegalArgumentException("Can't register empty components");
    }
    //register components to instances fields
    Arrays.stream(components).forEach(component -> {
      component.setMediator(this);
      switch (component.getKey()) {
        case Plaintext.KEY:
          plaintext = (Plaintext) component;
          break;
        case Ciphertext.KEY:
          ciphertext = (Ciphertext) component;
          break;
        case PlaintextArea.KEY:
          plaintextArea = (PlaintextArea) component;
          break;
        case CiphertextArea.KEY:
          ciphertextArea = (CiphertextArea) component;
          break;
        default:
      }
    });
  }


  public void encrypt(Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
    byte[] pt = getPlaintext();
    if (ArrayUtils.isNotEmpty(pt)) {
      byte[] ct = cipher.doFinal(pt);
      log.debug("EC-Plaintext: len={}, content={} | Ciphertext: len={}, content={}", pt.length, pt,
          ct.length, ct);
      setCiphertext(ct);
      setCiphertextArea(ct);
    } else {
      log.debug("Empty plaintext !!!");
    }
  }

  private byte[] getPlaintext() {
    return plaintext.getValue();
  }

  private void setPlaintext(byte[] array) {
    plaintext.setValue(array);
  }

  private void setCiphertextArea(byte[] array) {
    ciphertextArea.setValue(array);
  }


  public void decrypt(Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
    byte[] ct = getCiphertext();
    if (ArrayUtils.isNotEmpty(ct)) {
      byte[] pt = cipher.doFinal(ct);
      log.debug("DC-Plaintext: len={}, content={} | Ciphertext: len={}, content={}", pt.length, pt,
          ct.length, ct);
      setPlaintext(pt);
      setPlaintextArea(pt);
    } else {
      log.debug("Empty ciphertext !!!");
    }
  }

  private byte[] getCiphertext() {
    return ciphertext.getValue();
  }

  private void setCiphertext(byte[] array) {
    ciphertext.setValue(array);
  }

  private void setPlaintextArea(byte[] array) {
    plaintextArea.setValue(array);
  }

  //clear all value containers
  public void clear() {
    ciphertext.clear();
    plaintext.clear();
    ciphertextArea.clear();
    plaintextArea.clear();
  }


}
