package io.wonderland.rh.cipher;

import io.wonderland.rh.base.fx.base.BaseVBox;
import io.wonderland.rh.base.observer.KeygenObserver;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.key.KeySourcePane;
import io.wonderland.rh.utils.GuiUtils;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CipherPane extends BaseVBox {

  static final String CIPHER_KEY_PANE = "Cipher key";
  static final String CIPHER_MESSAGE_PANE = "Cipher message";

  private final VBox infoBox = new VBox();
  private final KeygenObserver keygenObserver = new KeygenObserver();
  private final KeySourcePane keySourcePane;
  private final CipherMessagePane cipherMessagePane;

  public CipherPane(String cspName, String cipherName)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    this.keySourcePane = new KeySourcePane(CIPHER_KEY_PANE, cipherName, keygenObserver);
    this.cipherMessagePane = new CipherMessagePane(CIPHER_MESSAGE_PANE, cspName, cipherName,
        keygenObserver);
    this.build(cspName, cipherName);
  }

  public CipherPane(String cspName, String cipherName, double width, double height)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    Scene scene = new Scene(this, width, height);
    Stage stage = new Stage();
    this.keySourcePane = new KeySourcePane(CIPHER_KEY_PANE, cipherName, keygenObserver);
    this.cipherMessagePane = new CipherMessagePane(CIPHER_MESSAGE_PANE, cspName, cipherName,
        keygenObserver);
    this.build(cspName, cipherName);
    stage.setScene(scene);
    stage.setTitle("CIPHER WINDOW : " + cipherName);
    stage.show();
  }


  private void build(String cspName, String cipherName)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    this.updateInfoBox(cspName, cipherName);
    this.getChildren().addAll(infoBox, keySourcePane, cipherMessagePane);

  }

  protected Optional<Cipher> getCipherInstance(String cspName, String serviceName)
      throws ServiceException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    Cipher tmp = Cipher.getInstance(serviceName, cspName);
    log.info("Selected cipher '{}' - provider '{}' ", tmp.getAlgorithm(),
        tmp.getProvider().getName());
    return Optional.of(tmp);
  }

  protected void updateInfoBox(String cspName, String cipherName)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
    Optional<Cipher> optionalCipher = getCipherInstance(cspName, cipherName);
    if (optionalCipher.isPresent()) {
      Cipher c = optionalCipher.get();
      HBox cipherNameBox = new HBox(GuiUtils.getTitle("Cipher : "), new Label(c.getAlgorithm()));
      HBox blockSizeBox = new HBox(GuiUtils.getTitle("Block size : "),
          new Label("" + c.getBlockSize()));
      HBox providerBox = new HBox(GuiUtils.getTitle("CSP : "),
          new Label(c.getProvider().getName() + "-" + c.getProvider().getVersionStr()));
      HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "),
          new Label(c.getProvider().getInfo()));
      this.infoBox.getChildren().addAll(cipherNameBox, blockSizeBox, providerBox, otherInfoBox);
    }
  }

}

