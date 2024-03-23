package io.wonderland.rh.cipher;

import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.base.dropdown.Dropdown;
import io.wonderland.rh.base.dropdown.DropdownHelper;
import io.wonderland.rh.base.pane.TextPane;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.keygen.KeygenObserver;
import java.io.File;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CipherMessagePane extends TitledPane {

  private static final int SPACING = 10;
  private final Stage stage;
  private final Alert alert = new Alert(AlertType.NONE);
  private final VBox controlBox = new VBox();
  private final HBox textBox = new HBox();
  private final VBox rootBox = new VBox();
  private final TextArea cipherTextArea = new TextArea();
  private final TextArea plainTextArea = new TextArea();

  //Observers of content arrays
  private final TypeObserver<byte[]> ciphertextObserver=new TypeObserver<>();
  private final TypeObserver<byte[]> plaintextObserver=new TypeObserver<>() ;
  private final Dropdown<String, byte[], TextArea> ciphertextDropdown = DropdownHelper.getEncodingDropdown(
      cipherTextArea, ciphertextObserver);
  private final Dropdown<String,byte[],TextArea> plaintextDropdown=DropdownHelper.getCharsetDropdown(plainTextArea,plaintextObserver);
  //text panes
  private final TextPane plaintextPane = new TextPane("Plaintext", plaintextDropdown,plainTextArea);
  private final TextPane ciphertextPane = new TextPane( "Ciphertext",ciphertextDropdown, cipherTextArea);
  private final Button encryptBtn = new Button("encrypt");
  private final Button decryptBtn = new Button("decrypt");
  private final Button clearBtn = new Button("clear");
  private final Button exportBtn = new Button("export");

  //encrypt & decrypt cipher
  private final Optional<Cipher> optionalEC;
  private final Optional<Cipher> optionalDC;
  private final KeygenObserver keygenObserver;

  public CipherMessagePane(Stage stage, String title, String cipherName, KeygenObserver keygenObserver) {
    this.stage = stage;
    this.optionalEC = getCipherInstance(cipherName);
    this.optionalDC = getCipherInstance(cipherName);
    this.keygenObserver = keygenObserver;
    this.setText(title);
    this.build();

  }


  private void build() {
    this.buildControlBox();

    this.textBox.setSpacing(SPACING);
    this.textBox.getChildren().addAll(plaintextPane, ciphertextPane);
    HBox.setHgrow(plaintextPane, Priority.ALWAYS);
    HBox.setHgrow(ciphertextPane, Priority.ALWAYS);

    this.rootBox.setSpacing(5);
    this.rootBox.getChildren().addAll(controlBox,textBox);
    this.setContent(rootBox);
  }

  private void buildControlBox() {
    this.encryptBtn.setPrefWidth(100);
    this.decryptBtn.setPrefWidth(100);
    this.clearBtn.setPrefWidth(100);
    this.exportBtn.setPrefWidth(100);
    this.encryptBtn.setOnMousePressed(new EncryptBtnReleased());
    this.decryptBtn.setOnMousePressed(new DecryptBtnReleased());
    this.clearBtn.setOnMousePressed(new ClearBtnReleased());
    this.exportBtn.setOnMousePressed(new ExportBtnPressed());

    HBox buttonBox = new HBox();
    buttonBox.setSpacing(SPACING);
    buttonBox.getChildren().addAll(encryptBtn, decryptBtn, clearBtn, exportBtn);

    this.controlBox.getChildren().addAll(keygenObserver.getContainerBox(), buttonBox);
    this.controlBox.setSpacing(SPACING);
  }


  protected Optional<Cipher> getCipherInstance(String serviceName) throws ServiceException {
    Cipher tmp = null;
    try {
      tmp = Cipher.getInstance(serviceName);
      log.info("Selected cipher '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    } catch (Exception e) {
      log.error("Failed to instantiate cipher service '{}'", serviceName);
    }
    return Optional.ofNullable(tmp);
  }

  private void cipherInit() throws InvalidKeyException {
    if (keygenObserver.isUpdated() && keygenObserver.getOptionalKey().isPresent()) {
      Object keyRef = keygenObserver.getOptionalKey().get();
      if (keyRef instanceof SecretKey) {
        cipherInitWithSecretKey((SecretKey) keyRef);
      } else if (keyRef instanceof KeyPair) {
        KeyPair keyPair = (KeyPair) keyRef;
        if (keygenObserver.isPublicKeySelected()) {
          cipherInitWithKeyPair(keyPair.getPublic(), keyPair.getPrivate());
        } else {
          cipherInitWithKeyPair(keyPair.getPrivate(), keyPair.getPublic());
        }
      }
    }
  }

  private void cipherInitWithSecretKey(SecretKey secretKey) throws InvalidKeyException {
    //init encrypt cipher
    if (optionalEC.isPresent()) {
      Cipher ec = optionalEC.get();
      try {
        ec.init(Cipher.ENCRYPT_MODE, secretKey);
      } catch (Exception e) {
        log.error("Failed to init encrypt-cipher.", e);
        throw e;
      }
    }

    //init decrypt cipher
    if (optionalDC.isPresent()) {
      Cipher dc = optionalDC.get();
      try {
        dc.init(Cipher.DECRYPT_MODE, secretKey);
      } catch (Exception e) {
        log.error("Failed to init decrypt-cipher.", e);
        throw e;
      }
    }
  }


  private void cipherInitWithKeyPair(Key publicKey, Key privateKey) throws InvalidKeyException {
    //init encrypt cipher
    if (optionalEC.isPresent()) {
      Cipher ec = optionalEC.get();
      try {
        ec.init(Cipher.ENCRYPT_MODE, publicKey);
      } catch (Exception e) {
        log.error("Failed to init encrypt-cipher.", e);
        throw e;
      }
    }

    //init decrypt cipher
    if (optionalDC.isPresent()) {
      Cipher dc = optionalDC.get();
      try {
        dc.init(Cipher.DECRYPT_MODE, privateKey);
      } catch (Exception e) {
        log.error("Failed to init decrypt-cipher.", e);
        throw e;
      }
    }
  }


  class EncryptBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      if (StringUtils.isEmpty(plainTextArea.getText())) {
        return;
      }
      try {
        //init cipher
        cipherInit();
        Cipher encryptCipher = optionalEC.get();

        byte[] plaintext = plainTextArea.getText().getBytes();
        byte[] ciphertext = encryptCipher.doFinal(plaintext);
        ciphertextObserver.update(ciphertext);

        //update ciphertext area
        ciphertextDropdown.getSelectedDropdownElement().runConsumer(ciphertextObserver,cipherTextArea);

        log.debug("Plaintext: len={}, content={}",plaintext.length,plaintext);
        log.debug("Ciphertext: len={}, content={}",ciphertext.length,ciphertext);
      } catch (Exception e) {
        log.error(e.getMessage());
        alert.setAlertType(AlertType.ERROR);
        alert.show();
        alert.setHeaderText(e.getLocalizedMessage());
      }
    }
  }

  class DecryptBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      if (StringUtils.isEmpty(cipherTextArea.getText())) {
        return;
      }
      try {
        //init cipher
        cipherInit();
        Cipher decryptCipher = optionalDC.get();

        if(ciphertextObserver.getValue().isPresent()) {
          byte[] ciphertext = ciphertextObserver.getValue().get();
          byte[] plaintext = decryptCipher.doFinal(ciphertext);

          plainTextArea.clear();
          plainTextArea.setText(new String(plaintext));

          log.debug("Plaintext: len={}, content={}",plaintext.length,plaintext);
          log.debug("Ciphertext: len={}, content={}",ciphertext.length,ciphertext);
        }
      } catch (Exception e) {
        log.error(e.getMessage());
        alert.setAlertType(AlertType.ERROR);
        alert.show();
        alert.setHeaderText(e.getLocalizedMessage());
      }
    }
  }

  class ClearBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      CompletableFuture.runAsync(() -> {
        plainTextArea.clear();
        cipherTextArea.clear();
      });

    }
  }

  class ExportBtnPressed implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      DirectoryChooser dirChooser = new DirectoryChooser();
      dirChooser.setTitle("Export data");
      final File file = dirChooser.showDialog(stage);
      CompletableFuture.runAsync(() -> {
        storeInputText(plainTextArea.getText().getBytes(), file.getPath());
        storeOutputText(cipherTextArea.getText().getBytes(), file.getPath());
      });
    }

    //todo:
    private void storeCipherKey(Key key, String filePath) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, "rh_cipher.key"))) {
        os.write(key.getEncoded());
        os.flush();
      } catch (Exception e) {
        //do nothing yet
      }
    }

    private void storeInputText(byte[] inputContent, String filePath) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, "rh_plaintext"))) {
        os.write(inputContent);
        os.flush();
      } catch (Exception e) {
        //do nothing
      }
    }

    private void storeOutputText(byte[] outputContent, String filePath) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, "rh_ciphertext"))) {
        os.write(outputContent);
        os.flush();
      } catch (Exception e) {
        //do nothing
      }
    }
  }

}


