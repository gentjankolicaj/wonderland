package io.wonderland.rh.cipher;

import io.wonderland.rh.cipher.key.DefaultKeyPane;
import io.wonderland.rh.cipher.key.AbstractKeyPane;
import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.exception.ServiceException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.util.Map;
import java.util.function.Consumer;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

public class KeyPane extends TitledPane {
   private boolean keyStaleState =true;
   private String cipherName;
   private BorderPane rootPane=new BorderPane();

   private HTogglePane<RadioButton> keyOriginPane = new HTogglePane<>("Key origin : ", 10,s -> new RadioButton(s),
      Map.of("generate", () -> setGenerateKeyPane(), "insert", () -> setInsertKeyPane()));

  public KeyPane(String title,String cipherName){
    this.cipherName=cipherName;
    this.setText(title);
    this.build();
  }


  private void build(){
    this.rootPane.setTop(keyOriginPane);
    this.setContent(rootPane);
  }


  private void setGenerateKeyPane(){
    this.rootPane.setCenter(new GenerateKeyPane());

  }

  private void setInsertKeyPane(){
   updateInputKeyPane(cipherName);
  }

  class GenerateKeyPane extends BorderPane implements KeySource {

    @Override
    public Key getKey() {
      return null;
    }

    @Override
    public boolean removeKey() {
      return false;
    }
  }


  private void updateInputKeyPane(String cipher) {
    Class<?> keyPaneClass = CipherConstants.getKeyPaneMappings().computeIfAbsent(cipher, k -> DefaultKeyPane.class);
    try {
      Consumer<?> consumer = s -> setKeyStaleState(true);
      Constructor<?> constructor = keyPaneClass.getDeclaredConstructor(Consumer.class);
      AbstractKeyPane<?> newKeyPane = (AbstractKeyPane<?>) constructor.newInstance(consumer);

      this.rootPane.setCenter(newKeyPane);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new ServiceException("Failed to changed KeyGeneratorPane", e);
    }
  }

   void setKeyStaleState(boolean state) {
    this.keyStaleState =state;
  }

  boolean getKeyStaleState(){
    return keyStaleState;
  }

  Key getKey(){
    return ((KeySource)rootPane.getCenter()).getKey();
  }

  boolean clearKey(){
    return true;
  }


}





