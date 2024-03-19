package io.wonderland.rh.cipher;

import io.wonderland.common.Arrays;
import io.wonderland.rh.base.common.TextPane;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.keygen.KeygenObserver;
import java.io.File;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
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
public class MessagePane extends TitledPane {

  private static final int SPACING = 10;
  private final Stage stage;
  private Alert alert = new Alert(AlertType.NONE);
  private final VBox controlBox = new VBox();
  private final HBox textBox = new HBox();
  private final BorderPane rootPane = new BorderPane();
  private final TextArea plainTextArea = new TextArea();
  private final TextArea cipherTextArea = new TextArea();
  private final TextPane plaintextPane = new TextPane("Plaintext", plainTextArea);
  private final TextPane ciphertextPane = new TextPane("Ciphertext", cipherTextArea);
  private final Button encryptBtn = new Button("encrypt");
  private final Button decryptBtn = new Button("decrypt");
  private final Button clearBtn = new Button("clear");
  private final Button exportBtn = new Button("export");
  private final Optional<Cipher> optionalEC;
  private final Optional<Cipher> optionalDC;
  private final KeygenObserver keygenObserver;

  public MessagePane(Stage stage, String title, String cipherName, KeygenObserver keygenObserver) {
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

    this.rootPane.setTop(controlBox);
    this.rootPane.setCenter(textBox);
    this.setContent(rootPane);
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
    if (keygenObserver.isKeyUpdated()) {
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

        //update gui
        cipherTextArea.clear();
        cipherTextArea.setText(Base64.getEncoder().encodeToString(ciphertext));

        log.info("E-Plaintext len="+plaintext.length+","+ Arrays.getStringValueOf(plaintext,','));
        log.info("E-Ciphertext len="+ciphertext.length+"," + Arrays.getStringValueOf(ciphertext,','));
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

        byte[] ciphertext = Base64.getDecoder().decode(cipherTextArea.getText().getBytes());
        byte[] plaintext = decryptCipher.doFinal(ciphertext);

        plainTextArea.clear();
        plainTextArea.setText(new String(plaintext));

        log.info("D-Ciphertext len="+ciphertext.length+"," + Arrays.getStringValueOf(ciphertext,','));
        log.info("D-Plaintext len="+plaintext.length+","+ Arrays.getStringValueOf(plaintext,','));
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


