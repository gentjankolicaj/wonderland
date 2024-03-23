package io.wonderland.rh.mac;

import io.wonderland.rh.base.KeyObserver;
import io.wonderland.rh.base.SecretKeyObserver;
import io.wonderland.rh.base.pane.KeyPane;
import io.wonderland.rh.utils.GuiUtils;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MacPane extends VBox {

  private final VBox infoBox=new VBox();
  private final KeyObserver<SecretKey> keyObserver=new SecretKeyObserver() ;
  private final Stage stage;
  private final String macName;
  private final KeyPane keyPane;
  private final MacMessagePane macMessagePane;


  public MacPane(Stage stage, String macName) throws NoSuchAlgorithmException {
    this.stage=stage;
    this.macName=macName;
    this.keyPane=new KeyPane(stage,"MAC key",macName,keyObserver  );
    this.macMessagePane =new MacMessagePane("MAC message",macName,keyObserver);
    this.build();
  }

  public MacPane( String macName,double width,double height) throws NoSuchAlgorithmException {
    this.stage=new Stage();
    Scene scene=new Scene(this,width,height);
    this.macName=macName;
    this.keyPane=new KeyPane(stage,"MAC key",macName,keyObserver  );
    this.macMessagePane =new MacMessagePane("MAC message",macName,keyObserver);
    this.build();
    this.stage.setScene(scene);
    this.stage.setTitle("MAC WINDOW : "+macName);
    this.stage.show();
  }

  private void build() throws NoSuchAlgorithmException {
    this.updateInfoBox();
    this.getChildren().addAll(infoBox,keyPane, macMessagePane);
  }

  private void updateInfoBox() throws NoSuchAlgorithmException {
    Optional<Mac> optionalMac=MacMessagePane.getMacInstance(macName);
    if(optionalMac.isPresent()) {
      Mac mac= optionalMac.get();
      HBox macBox = new HBox(GuiUtils.getTitle("MAC : "),new Label(mac.getAlgorithm()) );
      HBox macLengthBox = new HBox(GuiUtils.getTitle("MAC length : "), new Label(mac.getMacLength()+" bits."));
      HBox providerBox = new HBox(
          GuiUtils.getTitle("CSP : "), new Label(mac.getProvider().getName() + "-" + mac.getProvider().getVersionStr()));
      HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "), new Label(mac.getProvider().getInfo()));
      infoBox.getChildren().addAll(macBox, macLengthBox, providerBox, otherInfoBox);
    }
  }


}
