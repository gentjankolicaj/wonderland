package io.wonderland.rh;


import com.amazon.corretto.crypto.provider.AmazonCorrettoCryptoProvider;
import io.wonderland.alice.jca.AliceProvider;
import io.wonderland.rh.base.fx.Dock;
import io.wonderland.rh.base.fx.base.BaseBorderPane;
import io.wonderland.rh.base.fx.base.BaseTabPane;
import io.wonderland.rh.menu.MenuBarUtil;
import io.wonderland.rh.monitor.JMXBase;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.conscrypt.OpenSSLProvider;

@Slf4j
public class RabbitHoleApplication extends Application {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    addSecurityProviders();
    launch(args);

  }

  private static void addSecurityProviders() throws NoSuchAlgorithmException {
    setCryptoPolicy();
    Security.addProvider(new BouncyCastleProvider());
    Security.addProvider(new BouncyCastlePQCProvider());
    Security.addProvider(new BouncyCastleJsseProvider());
    Security.addProvider(new OpenSSLProvider());
    Security.addProvider(new AmazonCorrettoCryptoProvider());
    Security.addProvider(new AliceProvider());
  }

  private static void setCryptoPolicy() throws NoSuchAlgorithmException {
    log.info("Default cryptographic key strength : {}",
        javax.crypto.Cipher.getMaxAllowedKeyLength("AES"));
    Security.setProperty("crypto.policy", "unlimited");
    log.info("Set cryptographic key strength : {}",
        javax.crypto.Cipher.getMaxAllowedKeyLength("AES"));
  }


  @Override
  public void start(Stage stage) {
    MenuBar menuBar = MenuBarUtil.getMenuBar();
    BorderPane parentPane = new BaseBorderPane();
    TabPane tabPane = new BaseTabPane();
    Dock dock = new Dock(tabPane);
    BorderPane.setMargin(dock, GlobalConstants.DEFAULT_INSETS);
    BorderPane.setMargin(tabPane, GlobalConstants.BORDER_PANE_INSETS);
    parentPane.setTop(menuBar);
    parentPane.setCenter(tabPane);
    parentPane.setLeft(dock);
    Scene scene = new Scene(parentPane, GlobalConstants.SCENE_WIDTH, GlobalConstants.SCENE_HEIGHT);
    stage.setScene(scene);
    stage.setTitle("Rabbit hole");
    stage.show();

    //Add javafx parent node to be used by jmx search
    JMXBase.addParentNode(parentPane);
  }

}
