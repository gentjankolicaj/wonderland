package io.wonderland.rh.keygen;


import io.wonderland.rh.exception.ServiceException;
import java.io.File;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeygenPane extends BorderPane {
  private static final String DEFAULT_SECRET_KEY_FILE_NAME = "secret_key";
  private static final String DEFAULT_PRIVATE_KEY_FILENAME = "private_key";
  public static final String DEFAULT_PUBLIC_KEY_FILENAME = "public_key";
  public static final String KEYGEN_ALGORITHM = "Keygen algorithm: ";
  private final HBox infoBox = new HBox();
  private Stage stage;
  private final Optional<Object> optionalGen;

  public KeygenPane(Stage stage, Optional<Object> optionalGen) {
    this.stage=stage;
    this.optionalGen=optionalGen;
    this.build();
  }

  private void build(){
   this.setTop(getToolPanel());
   this.updateToolPanel();
  }


  private VBox getToolPanel() {
    final VBox miscBox = new VBox();
    miscBox.setPadding(new Insets(5, 5, 5, 5));
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Keygen algorithm: ?"));

    //button
    HBox buttonBox = getButtonBox();

    miscBox.getChildren().addAll(this.infoBox, buttonBox);
    return miscBox;
  }

  private HBox getButtonBox() {
    HBox hBox = new HBox();

    Button keygenBtn = new Button("keygen");
    keygenBtn.setPrefWidth(120);
    keygenBtn.setOnMousePressed(new KeygenBtnReleased());

    Button exportBtn = new Button("export");
    exportBtn.setPrefWidth(120);
    exportBtn.setOnMousePressed(new ExportBtnReleased());

    hBox.setSpacing(10);
    hBox.getChildren().addAll(keygenBtn, exportBtn);
    return hBox;
  }

  private void updateToolPanel() {
    if (optionalGen.isPresent()) {
      Object service = optionalGen.get();
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      if (service instanceof KeyGenerator) {
        this.setCenter(new KeyGeneratorPane());
        this.infoBox.getChildren().add(new Label(KEYGEN_ALGORITHM + getKeyGeneratorDetails((KeyGenerator) service)));
      } else if (service instanceof KeyPairGenerator) {
        this.setCenter(new KeyPairGeneratorPane());
        this.infoBox.getChildren()
            .add(new Label(KEYGEN_ALGORITHM + getKeyPairGeneratorDetails((KeyPairGenerator) service)));
      }
    } else {
      this.infoBox.getChildren().add(new Label(KEYGEN_ALGORITHM));
    }
  }


  protected KeyGenerator getKeyGenerator(String cspName, String serviceName) throws ServiceException {
    try {
      KeyGenerator tmp = KeyGenerator.getInstance(serviceName, cspName);
      log.info("Selected KeyGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyGenerator service '{}'", serviceName);
    }
    return null;
  }


  protected KeyPairGenerator getKeyPairGenerator(String cspName, String serviceName) throws ServiceException {
    try {
      KeyPairGenerator tmp = KeyPairGenerator.getInstance(serviceName, cspName);
      log.info("Selected KeyPairGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyPairGenerator service '{}'", serviceName);
    }
    return null;
  }

  private String getKeyGeneratorDetails(KeyGenerator keyGenerator) {
    if (Objects.nonNull(keyGenerator)) {
      return keyGenerator.getAlgorithm()+"  , CSP: "+keyGenerator.getProvider().getName();
    }
    return StringUtils.EMPTY;
  }

  private String getKeyPairGeneratorDetails(KeyPairGenerator keyPairGenerator) {
    if (Objects.nonNull(keyPairGenerator)) {
      return keyPairGenerator.getAlgorithm()+"  , CSP: "+keyPairGenerator.getProvider().getName();
    }
    return StringUtils.EMPTY;
  }


  private BorderPane getParentPane(){
    return this;
  }


  private class KeygenBtnReleased implements EventHandler<Event> {
    @Override
    public void handle(Event event) {
      try {
        performKeygen();
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }

    /**
     * Generate key or key pair
     */
    private void performKeygen(){
      if(optionalGen.isPresent()) {
        Object generator= optionalGen.get();
        if (generator instanceof KeyGenerator) {
          KeyGenerator keyGenerator = (KeyGenerator) generator;
          KeyGeneratorPane keyGeneratorPane = (KeyGeneratorPane) getParentPane().getCenter();
          keyGeneratorPane.update(keyGenerator.generateKey());
        } else if (generator instanceof KeyPairGenerator) {
          KeyPairGenerator keyPairGenerator = (KeyPairGenerator) generator;
          KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) getParentPane().getCenter();
          keyPairGeneratorPane.update(keyPairGenerator.generateKeyPair());
        } else {
          log.warn("No 'SecretKey' or 'KeyPair' generated.");
        }
      }
    }
  }

  private class ExportBtnReleased implements EventHandler<Event>{
    @Override
    public void handle(Event event){
      try{
        performExport();
      }catch (Exception e){
        log.error(e.getMessage());
      }
    }

    private void performExport() {
      if(optionalGen.isPresent()) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Export data");
        final File file = dirChooser.showDialog(stage);
        Object generator= optionalGen.get();
        if (generator instanceof KeyGenerator) {
          KeyGeneratorPane keyGeneratorPane = (KeyGeneratorPane) getParentPane().getCenter();
          CompletableFuture.runAsync(() -> exportKey(keyGeneratorPane.getSecretKey(), file.getPath(),DEFAULT_SECRET_KEY_FILE_NAME));
        } else if (generator instanceof KeyPairGenerator) {
          KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) getParentPane().getCenter();
          CompletableFuture.runAsync(() -> {
            exportKey(keyPairGeneratorPane.getKeyPair().getPrivate(), file.getPath(),DEFAULT_PRIVATE_KEY_FILENAME);
            exportKey(keyPairGeneratorPane.getKeyPair().getPublic(), file.getPath(), DEFAULT_PUBLIC_KEY_FILENAME);
          });
        }
      }
    }

    private void exportKey(Key key, String filePath,String filename) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, filename))) {
        os.write(key.getEncoded());
        os.flush();
      } catch (Exception e) {
        //do nothing yet
      }
    }
  }

}
