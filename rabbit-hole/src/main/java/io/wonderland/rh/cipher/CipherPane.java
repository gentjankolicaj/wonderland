package io.wonderland.rh.cipher;

import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.keygen.KeygenObserver;
import io.wonderland.rh.utils.LabelUtils;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CipherPane extends VBox {

  private final VBox infoBox;
  public KeyPane keyPane;
  public MessagePane messagePane;
  private String cipherName;
  private final KeygenObserver keygenObserver=new KeygenObserver();

  public CipherPane(Stage stage, String cipherName) throws NoSuchPaddingException, NoSuchAlgorithmException{
    this.cipherName = cipherName;
    this.infoBox = new VBox();
    this.keyPane = new KeyPane(stage,"Cipher key", cipherName, keygenObserver);
    this.messagePane = new MessagePane(stage,"Message", cipherName,  keygenObserver);
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
    Optional<Cipher> optionalCipher = getCipherInstance(cipherName);
    if(optionalCipher.isPresent()) {
      Cipher c=optionalCipher.get();
      HBox cipherNameBox=new HBox(LabelUtils.getTitle("Cipher : "),new Label(c.getAlgorithm()));
      HBox blockSizeBox=new HBox(LabelUtils.getTitle("Block size : "),new Label(""+c.getBlockSize()));
      HBox providerBox=new HBox(LabelUtils.getTitle("CSP : "),new Label(c.getProvider().getName()+"-"+c.getProvider().getVersionStr()));
      HBox otherInfoBox=new HBox(LabelUtils.getTitle("Info : "),new Label(c.getProvider().getInfo()));
      this.infoBox.getChildren().addAll(cipherNameBox,blockSizeBox,providerBox,otherInfoBox);
    }
  }

}

