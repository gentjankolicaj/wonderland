package io.wonderland.rh.keystore;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.exception.KeystoreException;
import io.wonderland.rh.utils.GuiUtils;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeystorePane extends BorderPane {

  public static final String OPEN = "Open...";
  private final HBox controlContainer = new HBox();
  private final Label keystorePathLbl = new Label(OPEN);
  private final VBox dataContainer = new VBox();

  public KeystorePane() {
    build();
  }

  private void build() {
    this.keystorePathLbl.setOnMouseReleased(new KeystorePathEvent());
    this.keystorePathLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/opened-folder/icons8-opened-folder-24.png"));
    this.controlContainer.getChildren().add(keystorePathLbl);
    this.controlContainer.setSpacing(GlobalConstants.SPACING);
    this.controlContainer.setAlignment(Pos.CENTER);
    this.setTop(controlContainer);
    this.setCenter(dataContainer);
    BorderPane.setMargin(this, GlobalConstants.DEFAULT_INSETS);
  }

  private VBox createContainer(KSKeyPane ksKeyPane, KSCertPane ksCertPane) {
    VBox vBox = new VBox();
    vBox.setSpacing(GlobalConstants.SPACING);
    vBox.getChildren().addAll(ksKeyPane, ksCertPane);
    return vBox;
  }

  private class KeystorePathEvent implements EventHandler<Event> {

    static final String KEYSTORE_PATH = "Keystore path";
    static final String PLEASE_ENTER_KEYSTORE_PASSWORD = "Enter keystore password : ";

    @Override
    public void handle(Event event) {
      keystoreProcess();
    }

    private void keystoreProcess() {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(KEYSTORE_PATH);
      final File file = fileChooser.showOpenDialog(getScene().getWindow());
      try {
        if (StringUtils.isNotEmpty(file.getName())) {
          keystorePathLbl.setText(file.getName());
          TextInputDialog td = new TextInputDialog();
          td.setHeaderText(PLEASE_ENTER_KEYSTORE_PASSWORD);
          Optional<String> ksPwdOptional = td.showAndWait();
          if (ksPwdOptional.isPresent()) {
            String keystorePassword = ksPwdOptional.get();
            Optional<KeyStore> ksOptional = loadKeystore(file, keystorePassword);
            if (ksOptional.isPresent()) {
              updateKeyAndCertPanes(ksOptional.get());
            } else {
              throw new KeystoreException(
                  "Failed to open keystore after trying '" + KeyStoreType.values().length
                      + "' different types.");
            }
          } else {
            keystorePathLbl.setText(OPEN);
          }
        }
      } catch (Exception e) {
        keystorePathLbl.setText(OPEN);
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }

    private void updateKeyAndCertPanes(KeyStore keyStore) {
      if (Objects.nonNull(keyStore)) {
        try {
          KSKeyPane ksKeyPane = new KSKeyPane("Keys", keyStore);
          KSCertPane ksCertPane = new KSCertPane("Certs", keyStore);
          setCenter(createContainer(ksKeyPane, ksCertPane));
        } catch (Exception e) {
          log.error("", e);
          setCenter(new BorderPane());
        }
      }
    }

    private Optional<KeyStore> loadKeystore(File file, String keystorePassword) {
      try {
        //check if file suffix is known keystore type O(n^2)
        Optional<KeyStoreType> optional = getKeyStoreType(file);
        KeyStore keyStore = null;
        boolean loaded = false;
        if (optional.isPresent()) {
          keyStore = KeyStore.getInstance(optional.get().getName().toLowerCase());
          keyStore.load(new FileInputStream(file), keystorePassword.toCharArray());
          loaded = true;
        } else {
          for (KeyStoreType keyStoreType : KeyStoreType.values()) {
            try {
              keyStore = KeyStore.getInstance(keyStoreType.getName().toLowerCase());
              keyStore.load(new FileInputStream(file), keystorePassword.toCharArray());
              loaded = true;
              break;
            } catch (Exception e) {
              log.error(e.getMessage());
            }
          }
        }
        if (loaded) {
          return Optional.of(keyStore);
        } else {
          return Optional.empty();
        }
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
        return Optional.empty();
      }
    }

    private Optional<KeyStoreType> getKeyStoreType(File file) {
      char separator = File.separatorChar;
      String pathname = file.getPath();
      int len = pathname.length() - 1;
      Optional<KeyStoreType> optional = Optional.empty();
      for (int i = len; i >= 0; i--) {
        if (pathname.charAt(i) != separator) {
          String subs = pathname.substring(i, len + 1);
          optional = Arrays.stream(KeyStoreType.values())
              .filter(e -> subs.toLowerCase().contains(e.getName().toLowerCase()))
              .findAny();
          if (optional.isPresent()) {
            break;
          }
        }
      }
      return optional;
    }
  }

}
