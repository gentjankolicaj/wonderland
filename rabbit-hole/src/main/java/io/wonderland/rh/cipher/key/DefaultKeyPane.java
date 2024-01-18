package io.wonderland.rh.cipher.key;


import java.security.Key;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultKeyPane<T extends Key> extends TextAreaKeyPane<T> {

  public DefaultKeyPane(String keyLabel, Consumer cipherStateConsumer) {
    super(keyLabel, cipherStateConsumer);
  }

  public DefaultKeyPane(Consumer cipherStateConsumer) {
    super("Default", cipherStateConsumer);
  }
}
