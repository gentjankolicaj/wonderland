package io.wonderland.rh.key;


import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.key.secret.AbstractKeyPane;
import io.wonderland.rh.key.secret.DefaultSecretKeyPane;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.StringUtils;

public class KeyInputPane extends BorderPane {

  private final String cipherName;
  private final EitherKeyObserver eitherKeyObserver;

  KeyInputPane(String cipherName, EitherKeyObserver eitherKeyObserver) {
    this.cipherName = cipherName;
    this.eitherKeyObserver = eitherKeyObserver;
    this.build();
  }

  private void build() {
    createPane(cipherName, eitherKeyObserver);
  }


  private void createPane(String cipherName, EitherKeyObserver eitherKeyObserver) {
    Class<?> keyPaneClass;
    if (StringUtils.isEmpty(cipherName)) {
      keyPaneClass = DefaultSecretKeyPane.class;
    } else {
      keyPaneClass = KeyConstants.getKeyInputPaneMap()
          .getOrDefault(cipherName, DefaultSecretKeyPane.class);
    }
    try {
      Constructor<?> constructor = keyPaneClass.getDeclaredConstructor(EitherKeyObserver.class);
      AbstractKeyPane<?> pane = (AbstractKeyPane<?>) constructor.newInstance(eitherKeyObserver);
      this.setCenter(pane);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
             IllegalAccessException e) {
      throw new ServiceException("Failed to changed SecretKeyGeneratorPane", e);
    }
  }
}