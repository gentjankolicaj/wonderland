package io.wonderland.rh.keygen;


import io.wonderland.rh.base.Observer;
import io.wonderland.rh.utils.GuiUtils;
import java.io.File;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class KeygenPane extends BorderPane  {
  private static final String DEFAULT_SECRET_KEY_FILE_NAME = "secret_key";
  private static final String DEFAULT_PRIVATE_KEY_FILENAME = "private_key";
  public static final String DEFAULT_PUBLIC_KEY_FILENAME = "public_key";
  private final VBox miscBox = new VBox();
  private final KeygenObservable keygenObservable =new KeygenObservable() ;
  private final Stage stage;
  private final Optional<Object> optionalGen;

  public KeygenPane(Stage stage, Optional<Object> optionalGen) {
    this.stage=stage;
    this.optionalGen=optionalGen;
    this.build();
  }

  public KeygenPane(Stage stage, Optional<Object> optionalGen,Observer<Integer,Object>...observers) {
    Objects.requireNonNull(observers);
    this.stage=stage;
    this.optionalGen=optionalGen;
    this.keygenObservable.addAllObservers(observers);
    this.build();
  }

  public KeygenPane(Optional<Object> optionalGen,double width,double height) {
    this.stage=new Stage();
    Scene scene=new Scene(this,width,height);
    this.optionalGen=optionalGen;
    this.build();
    this.stage.setScene(scene);
    this.stage.setTitle("KEYGEN WINDOW");
    this.stage.show();
  }

  private void build(){
   this.buildToolPanel();
   this.setTop(miscBox);
   this.setCenter(getCenterPane());
  }

  private void buildToolPanel() {
    miscBox.setPadding(new Insets(5, 5, 5, 5));
    miscBox.setSpacing(10);

    //info labels
    VBox infoBox=getInfoBox();

    //button
    HBox buttonBox = getButtonBox();

    miscBox.getChildren().addAll(infoBox, buttonBox);
  }

  private BorderPane getCenterPane(){
    if(optionalGen.isPresent()){
      Object service = optionalGen.get();
      if (service instanceof KeyGenerator) {
        return new KeyGeneratorPane();
      } else if (service instanceof KeyPairGenerator) {
        return new KeyPairGeneratorPane();
      }
    }
    return new BorderPane();
  }

  private VBox getInfoBox(){
    VBox infoBox=new VBox();
    if(optionalGen.isPresent()){
      Object service = optionalGen.get();
      if (service instanceof KeyGenerator) {
        KeyGenerator kg=(KeyGenerator) service;
        HBox keygenNameBox=new HBox(GuiUtils.getTitle("Algorithm : "),new Label(kg.getAlgorithm()));
        HBox providerBox=new HBox(GuiUtils.getTitle("CSP : "),new Label(kg.getProvider().getName()+"-"+kg.getProvider().getVersionStr()));
        HBox otherInfoBox=new HBox(GuiUtils.getTitle("Info : "),new Label(kg.getProvider().getInfo()));
        infoBox.getChildren().addAll(keygenNameBox,providerBox,otherInfoBox);

      } else if (service instanceof KeyPairGenerator) {
        KeyPairGenerator kg=(KeyPairGenerator) service;
        HBox keygenNameBox=new HBox(GuiUtils.getTitle("Algorithm : "),new Label(kg.getAlgorithm()));
        HBox providerBox=new HBox(GuiUtils.getTitle("CSP : "),new Label(kg.getProvider().getName()+"-"+kg.getProvider().getVersionStr()));
        HBox otherInfoBox=new HBox(GuiUtils.getTitle("Info : "),new Label(kg.getProvider().getInfo()));
        infoBox.getChildren().addAll(keygenNameBox,providerBox,otherInfoBox);
      }
    }
    return infoBox;
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


  private BorderPane getParentPane(){
    return this;
  }

   class KeygenBtnReleased implements EventHandler<Event> {
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
          //notify observers & update pane
          SecretKey secretKey=keyGenerator.generateKey()  ;
          keygenObservable.notifyObservers(secretKey);
          keyGeneratorPane.update(secretKey);
        } else if (generator instanceof KeyPairGenerator) {
          KeyPairGenerator keyPairGenerator = (KeyPairGenerator) generator;
          KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) getParentPane().getCenter();
          KeyPair keyPair=keyPairGenerator.generateKeyPair();
          //notify observers & update pane
          keygenObservable.notifyObservers(keyPair);
          keyPairGeneratorPane.update(keyPair);
        } else {
          log.warn("No 'SecretKey' or 'KeyPair' generated.");
        }
      }
    }
  }

   class ExportBtnReleased implements EventHandler<Event>{
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
