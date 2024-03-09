package io.wonderland.rh.cipher;

import io.wonderland.rh.exception.ServiceException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CipherPane extends VBox {

  private final HBox infoBox;
  public KeyPane keyPane;
  public MessagePane messagePane;
  private String cipherName;

  public CipherPane(Stage stage, String cipherName) throws NoSuchPaddingException, NoSuchAlgorithmException{
    this.cipherName = cipherName;
    this.infoBox = new HBox();
    this.keyPane = new KeyPane(stage,"Cipher key", cipherName);
    this.messagePane = new MessagePane(stage,"Message", cipherName, keyPane);
    this.build();
  }

  private void build() throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.updateInfoBox();
    this.getChildren().addAll(infoBox,keyPane,messagePane);

  }

  protected Optional<Cipher> getCipherInstance(String serviceName)
      throws ServiceException, NoSuchPaddingException, NoSuchAlgorithmException {
    Cipher tmp = Cipher.getInstance(serviceName);
      log.info("Selected cipher '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    return Optional.ofNullable(tmp);
  }

  protected void updateInfoBox() throws NoSuchPaddingException, NoSuchAlgorithmException {
    if (!infoBox.getChildren().isEmpty()) {
      this.infoBox.getChildren().remove(0);
    }
    Optional<Cipher> optionalCipher = getCipherInstance(cipherName);
    optionalCipher.ifPresent(c -> {
      Label label=new Label("Cipher: " + c.getAlgorithm() + " , block-size : " + c.getBlockSize() + " , CSP: "
          + c.getProvider().getName());
      label.setFont(Font.font("ARIAL", FontWeight.BOLD, 15));
      this.infoBox.getChildren().add(label);
    });
  }


}

