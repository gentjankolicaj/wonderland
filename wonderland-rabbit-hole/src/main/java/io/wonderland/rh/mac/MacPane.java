package io.wonderland.rh.mac;

import io.wonderland.rh.base.fx.base.BaseVBox;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.key.KeySourcePane;
import io.wonderland.rh.utils.GuiUtils;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.crypto.Mac;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MacPane extends BaseVBox {

  private final VBox infoBox = new VBox();
  private final EitherKeyObserver eitherKeyObserver = new EitherKeyObserver();
  private final KeySourcePane keySourcePane;
  private final MacMessagePane macMessagePane;


  public MacPane(String cspName, String macName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this.keySourcePane = new KeySourcePane("MAC key", eitherKeyObserver);
    this.macMessagePane = new MacMessagePane("MAC message", cspName, macName, eitherKeyObserver);
    this.build(cspName, macName);
  }

  public MacPane(String cspName, String macName, double width, double height)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    Stage stage = new Stage();
    Scene scene = new Scene(this, width, height);
    this.keySourcePane = new KeySourcePane("MAC key", eitherKeyObserver);
    this.macMessagePane = new MacMessagePane("MAC message", cspName, macName, eitherKeyObserver);
    this.build(cspName, macName);
    stage.setScene(scene);
    stage.setTitle("MAC WINDOW : " + macName);
    stage.show();
  }

  private void build(String cspName, String macName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this.buildInfoBox(cspName, macName);
    VBox.setVgrow(macMessagePane, Priority.ALWAYS);
    this.getChildren().addAll(infoBox, keySourcePane, macMessagePane);
  }

  private void buildInfoBox(String cspName, String macName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    Optional<Mac> optionalMac = MacMessagePane.getMacInstance(cspName, macName);
    if (optionalMac.isPresent()) {
      Mac mac = optionalMac.get();
      HBox macBox = new HBox(GuiUtils.getTitle("MAC : "), new Label(mac.getAlgorithm()));
      HBox macLengthBox = new HBox(GuiUtils.getTitle("MAC length : "),
          new Label(mac.getMacLength() + " bits."));
      HBox providerBox = new HBox(
          GuiUtils.getTitle("CSP : "),
          new Label(mac.getProvider().getName() + "-" + mac.getProvider().getVersionStr()));
      HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "),
          new Label(mac.getProvider().getInfo()));
      infoBox.getChildren().addAll(macBox, macLengthBox, providerBox, otherInfoBox);
    }
  }

}
