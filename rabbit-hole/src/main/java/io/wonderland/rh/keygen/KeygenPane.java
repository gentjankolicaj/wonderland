package io.wonderland.rh.keygen;


import io.atlassian.fugue.Either;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.base.observer.KeygenObservable;
import io.wonderland.rh.export.KeyBox;
import io.wonderland.rh.export.KeyBox.KeyParams;
import io.wonderland.rh.export.KeyExportDialog;
import io.wonderland.rh.export.KeystoreBox;
import io.wonderland.rh.export.KeystoreBox.KeystoreParams;
import io.wonderland.rh.utils.GuiUtils;
import io.wonderland.rh.utils.KeyUtils;
import io.wonderland.rh.utils.KeystoreUtils;
import java.io.File;
import java.security.KeyPairGenerator;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeygenPane extends BorderPane {

  private final KeygenObservable keygenObservable = new KeygenObservable();
  private final VBox miscBox = new VBox();
  private final BorderPane centerPane;
  private final Either<KeyGenerator, KeyPairGenerator> keygen;

  public KeygenPane(Either<KeyGenerator, KeyPairGenerator> keygen) {
    this.keygen = keygen;
    this.centerPane = createCenterPane(keygen, keygenObservable);
    this.build();
  }

  public KeygenPane(Either<KeyGenerator, KeyPairGenerator> keygen, EitherKeyObserver... observers) {
    Objects.requireNonNull(observers);
    this.keygen = keygen;
    this.keygenObservable.addAllObservers(observers);
    this.centerPane = createCenterPane(keygen, keygenObservable);
    this.build();
  }

  public KeygenPane(Either<KeyGenerator, KeyPairGenerator> keygen, double width, double height) {
    Stage stage = new Stage();
    Scene scene = new Scene(this, width, height);
    this.keygen = keygen;
    this.centerPane = createCenterPane(keygen, keygenObservable);
    this.build();
    stage.setScene(scene);
    stage.setTitle("KEYGEN WINDOW");
    stage.show();
  }

  private void build() {
    this.buildMisc();
    this.setTop(miscBox);
    this.setCenter(centerPane);
  }

  private void buildMisc() {
    miscBox.setPadding(new Insets(5, 5, 5, 5));
    miscBox.setSpacing(10);

    //info labels
    VBox infoBox = getInfoBox();

    //button
    HBox buttonBox = getButtonBox();
    miscBox.getChildren().addAll(infoBox, buttonBox);
  }

  private BorderPane createCenterPane(Either<KeyGenerator, KeyPairGenerator> keygen,
      KeygenObservable keygenObservable) {
    if (Objects.nonNull(keygen)) {
      if (keygen.isLeft()) {
        return new SecretKeyGeneratorPane(keygen.left().get(), keygenObservable);
      } else if (keygen.isRight()) {
        return new KeyPairGeneratorPane(keygen.right().get(), keygenObservable);
      }
    }
    return new BorderPane();
  }

  private VBox getInfoBox() {
    VBox infoBox = new VBox();
    if (Objects.nonNull(keygen)) {
      if (keygen.isLeft()) {
        KeyGenerator kg = keygen.left().get();
        HBox keygenNameBox = new HBox(GuiUtils.getTitle("Algorithm : "),
            new Label(kg.getAlgorithm()));
        HBox providerBox = new HBox(GuiUtils.getTitle("CSP : "),
            new Label(kg.getProvider().getName() + "-" + kg.getProvider().getVersionStr()));
        HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "),
            new Label(kg.getProvider().getInfo()));
        infoBox.getChildren().addAll(keygenNameBox, providerBox, otherInfoBox);

      } else if (keygen.isRight()) {
        KeyPairGenerator kg = keygen.right().get();
        HBox keygenNameBox = new HBox(GuiUtils.getTitle("Algorithm : "),
            new Label(kg.getAlgorithm()));
        HBox providerBox = new HBox(GuiUtils.getTitle("CSP : "),
            new Label(kg.getProvider().getName() + "-" + kg.getProvider().getVersionStr()));
        HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "),
            new Label(kg.getProvider().getInfo()));
        infoBox.getChildren().addAll(keygenNameBox, providerBox, otherInfoBox);
      }
    }
    return infoBox;
  }

  private HBox getButtonBox() {
    HBox hBox = new HBox();

    Button generateBtn = new Button("generate");
    generateBtn.setPrefWidth(120);
    generateBtn.setOnMousePressed(new GenerateBtnReleased());

    Button exportBtn = new Button("export");
    exportBtn.setPrefWidth(120);
    exportBtn.setOnMousePressed(new ExportBtnReleased());

    hBox.setSpacing(10);
    hBox.getChildren().addAll(generateBtn, exportBtn);
    return hBox;
  }

  private BorderPane getParentPane() {
    return this;
  }


  private class GenerateBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        callKeygen();
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }

    /**
     * Generate key or key pair
     */
    private void callKeygen() {
      if (Objects.nonNull(keygen)) {
        if (keygen.isLeft()) {
          SecretKeyGeneratorPane secretKeyGeneratorPane = (SecretKeyGeneratorPane) centerPane;
          secretKeyGeneratorPane.generate();
        } else if (keygen.isRight()) {
          KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) centerPane;
          keyPairGeneratorPane.generate();
        } else {
          log.warn("No 'SecretKey' or 'KeyPair' generated.");
        }
      }
    }
  }

  private class ExportBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        KeyExportDialog keyExportDialog = new KeyExportDialog();
        Optional<Either<KeyBox, KeystoreBox>> optional = keyExportDialog.showAndWait();
        if (optional.isPresent()) {
          Either<KeyBox, KeystoreBox> either = optional.get();
          if (either.isLeft()) {
            KeyParams keyParams = either.left().get().getKeyParams();
            if (keygen.isLeft()) {
              SecretKeyGeneratorPane secretKeyGeneratorPane = (SecretKeyGeneratorPane) getParentPane().getCenter();
              KeyUtils.exportKey(secretKeyGeneratorPane.getSecretKey(),
                  createSKPathname(keyParams));
            } else if (keygen.isRight()) {
              KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) getParentPane().getCenter();
              KeyUtils.exportKey(keyPairGeneratorPane.getKeyPair().getPrivate(),
                  createSKPathname(keyParams));
              KeyUtils.exportKey(keyPairGeneratorPane.getKeyPair().getPublic(),
                  createPKPathname(keyParams));
            }
          } else {
            KeystoreParams keystoreParams = either.right().get().getKeystoreParams();
            if (keygen.isLeft()) {
              SecretKeyGeneratorPane secretKeyGeneratorPane = (SecretKeyGeneratorPane) getParentPane().getCenter();
              KeystoreUtils.exportSecretKey(secretKeyGeneratorPane.getSecretKey(), keystoreParams);
            } else if (keygen.isRight()) {
              KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) getParentPane().getCenter();
              KeystoreUtils.exportKeyPair(keyPairGeneratorPane.getKeyPair(), keystoreParams);
            }
          }
        } else {
          keyExportDialog.hide();
        }
      } catch (Exception e) {
        ExceptionDialog exceptionDialog = new ExceptionDialog(e);
        exceptionDialog.showAndWait();
      }
    }

    private String createSKPathname(KeyParams keyParams) {
      String localTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss-nn"));
      return keyParams.getKeyPath() + File.separatorChar + "SK-" + localTime + "-"
          + keyParams.getKeyFilename();

    }

    private String createPKPathname(KeyParams keyParams) {
      String localTime = LocalTime.now().format(DateTimeFormatter.ofPattern("-HH-mm-ss-nn"));
      return keyParams.getKeyPath() + File.separatorChar + "PK-" + localTime + "-"
          + keyParams.getKeyFilename();

    }
  }

}