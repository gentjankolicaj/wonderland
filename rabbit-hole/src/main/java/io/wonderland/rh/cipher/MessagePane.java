package io.wonderland.rh.cipher;

import io.wonderland.rh.common.TextPane;
import io.wonderland.rh.exception.ServiceException;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MessagePane extends TitledPane {

  private final Stage stage;
  private HBox buttonBox = new HBox();
  private HBox textBox = new HBox();
  private BorderPane rootPane = new BorderPane();
  private final TextArea plainTextArea = new TextArea();
  private final TextArea cipherTextArea = new TextArea();
  private final TextPane plaintextPane=new TextPane("Plaintext", plainTextArea);
  private final TextPane ciphertextPane=new TextPane("Ciphertext",cipherTextArea);
  private Button encryptBtn = new Button("encrypt");
  private Button decryptBtn = new Button("decrypt");
  private Button clearBtn = new Button("clear");
  private Button exportBtn = new Button("export");
  private Optional<Cipher> optionalEC;
  private Optional<Cipher> optionalDC;
  private KeyPane keyPane;

  public MessagePane(Stage stage, String title, String cipherName, KeyPane keyPane) {
    this.stage = stage;
    this.keyPane = keyPane;
    this.optionalEC = getCipherInstance(cipherName);
    this.optionalDC = getCipherInstance(cipherName);
    this.setText(title);
    this.build();

  }

  private void build() {
    this.encryptBtn.setPrefWidth(100);
    this.decryptBtn.setPrefWidth(100);
    this.clearBtn.setPrefWidth(100);
    this.exportBtn.setPrefWidth(100);
    this.encryptBtn.setOnMousePressed(new EncryptBtnReleased());
    this.decryptBtn.setOnMousePressed(new DecryptBtnReleased());
    this.clearBtn.setOnMousePressed(new ClearBtnReleased());
    this.exportBtn.setOnMousePressed(new ExportBtnPressed());

    this.buttonBox.setSpacing(10);
    this.buttonBox.getChildren().addAll(encryptBtn, decryptBtn, clearBtn, exportBtn);

    this.textBox.setSpacing(10);
    this.textBox.getChildren().addAll(plaintextPane,ciphertextPane);
    HBox.setHgrow(plaintextPane, Priority.ALWAYS);
    HBox.setHgrow(ciphertextPane,Priority.ALWAYS);

    this.rootPane.setTop(buttonBox);
    this.rootPane.setCenter(textBox);
    this.setContent(rootPane);
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
    if (this.keyPane.getKeyStaleState()) {

    }
    //init encrypt cipher
    if (optionalEC.isPresent()) {
      Cipher ec = optionalEC.get();
      try {
        ec.init(Cipher.ENCRYPT_MODE, keyPane.getKey());
      } catch (Exception e) {
        log.error("Failed to init encrypt-cipher.", e);
        throw e;
      }
    }

    //init decrypt cipher
    if (optionalDC.isPresent()) {
      Cipher dc = optionalDC.get();
      try {
        dc.init(Cipher.DECRYPT_MODE, keyPane.getKey());
      } catch (Exception e) {
        log.error("Failed to init decrypt-cipher.", e);
        throw e;
      }

      //set a stale state to false, because ciphers are initialized with last keys
      this.keyPane.setKeyStaleState(false);
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

        String plaintext = plainTextArea.getText();
        byte[] ciphertext = encryptCipher.doFinal(plaintext.getBytes());

        //update gui
        cipherTextArea.clear();
        cipherTextArea.setText(new String(ciphertext, StandardCharsets.UTF_8));

        log.info("" + keyPane.getKey());
        log.info("Ciphertext " + new String(ciphertext));
      } catch (Exception e) {
        log.error(e.getMessage());
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
        String ciphertext = cipherTextArea.getText();
        byte[] plaintext = decryptCipher.doFinal(ciphertext.getBytes());
        plainTextArea.clear();
        plainTextArea.setText(new String(plaintext, StandardCharsets.UTF_8));

        log.info("" + keyPane.getKey());
        log.info("Plaintext " + new String(plaintext));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

  class ClearBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      CompletableFuture.runAsync(() -> {
        keyPane.clearKey();
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
        storeCipherKey(keyPane.getKey(), file.getPath());
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
