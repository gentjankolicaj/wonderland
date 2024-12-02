package io.wonderland.rh.key;


import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.HToggleBox;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import java.util.Map;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeySourcePane extends TitledPane {

  private final BorderPane container = new BorderPane();
  private final EitherKeyObserver eitherKeyObserver;
  private String cipherName;
  private final HToggleBox<RadioButton> keyOriginTypes = new HToggleBox<>("Key source : ", 10,
      RadioButton::new,
      Map.of("generator", this::setKeyGeneratorPane, "keystore", this::setKeystorePane,
          "text", this::setKeyInputPane));

  public KeySourcePane(String title, EitherKeyObserver eitherKeyObserver) {
    this.eitherKeyObserver = eitherKeyObserver;
    this.setText(title);
    this.build();
  }

  public KeySourcePane(String title, String cipherName, EitherKeyObserver eitherKeyObserver) {
    this.cipherName = cipherName;
    this.eitherKeyObserver = eitherKeyObserver;
    this.setText(title);
    this.build();
  }


  private void build() {
    this.container.setTop(keyOriginTypes);
    this.setContent(container);
  }

  private void setKeyGeneratorPane() {
    KeyGeneratorPane keyGeneratorPane = new KeyGeneratorPane(eitherKeyObserver);
    BorderPane.setMargin(keyGeneratorPane, GlobalConstants.DEFAULT_INSETS);
    this.container.setCenter(keyGeneratorPane);
  }

  private void setKeyInputPane() {
    KeyInputPane keyInputPane = new KeyInputPane(cipherName, eitherKeyObserver);
    BorderPane.setMargin(keyInputPane, GlobalConstants.DEFAULT_INSETS);
    this.container.setCenter(keyInputPane);
  }

  private void setKeystorePane() {
    KeyStorePane keyStorePane = new KeyStorePane(eitherKeyObserver);
    BorderPane.setMargin(keyStorePane, GlobalConstants.DEFAULT_INSETS);
    this.container.setCenter(keyStorePane);
  }

}





