package io.wonderland.rh.cipher;

import io.wonderland.rh.base.common.KeyPane;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.keygen.KeygenObserver;
import io.wonderland.rh.utils.GuiUtils;
import java.security.NoSuchAlgorithmException;
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
public class CipherPane extends VBox {

  private final VBox infoBox= new VBox();
  private final KeyPane keyPane;
  private final CipherMessagePane cipherMessagePane;
  private final KeygenObserver keygenObserver=new KeygenObserver();

  public CipherPane(Stage stage, String cipherName) throws NoSuchPaddingException, NoSuchAlgorithmException{
    this.keyPane = new KeyPane(stage,"Cipher key", cipherName, keygenObserver);
    this.cipherMessagePane = new CipherMessagePane(stage,"Cipher message", cipherName,  keygenObserver);
    this.build(cipherName);
  }

  public CipherPane(String cipherName,double width,double height) throws NoSuchPaddingException, NoSuchAlgorithmException{
    Scene scene=new Scene(this,width,height);
    Stage stage=new Stage();
    this.keyPane = new KeyPane(stage,"Cipher key", cipherName, keygenObserver);
    this.cipherMessagePane = new CipherMessagePane(stage,"Cipher message", cipherName,  keygenObserver);
    this.build(cipherName);
    stage.setScene(scene);
    stage.setTitle("CIPHER WINDOW : "+cipherName);
    stage.show();
  }



  private void build(String cipherName) throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.updateInfoBox(cipherName);
    this.getChildren().addAll(infoBox,keyPane, cipherMessagePane);

  }

  protected Optional<Cipher> getCipherInstance(String serviceName)
      throws ServiceException, NoSuchPaddingException, NoSuchAlgorithmException {
    Cipher tmp = Cipher.getInstance(serviceName);
      log.info("Selected cipher '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    return Optional.of(tmp);
  }

  protected void updateInfoBox(String cipherName) throws NoSuchPaddingException, NoSuchAlgorithmException {
    Optional<Cipher> optionalCipher = getCipherInstance(cipherName);
    if(optionalCipher.isPresent()) {
      Cipher c=optionalCipher.get();
      HBox cipherNameBox=new HBox(GuiUtils.getTitle("Cipher : "),new Label(c.getAlgorithm()));
      HBox blockSizeBox=new HBox(GuiUtils.getTitle("Block size : "),new Label(""+c.getBlockSize()));
      HBox providerBox=new HBox(GuiUtils.getTitle("CSP : "),new Label(c.getProvider().getName()+"-"+c.getProvider().getVersionStr()));
      HBox otherInfoBox=new HBox(GuiUtils.getTitle("Info : "),new Label(c.getProvider().getInfo()));
      this.infoBox.getChildren().addAll(cipherNameBox,blockSizeBox,providerBox,otherInfoBox);
    }
  }

}

