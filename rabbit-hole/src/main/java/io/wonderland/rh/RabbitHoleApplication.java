package io.wonderland.rh;

import io.wonderland.alice.jca.AliceProvider;
import io.wonderland.rh.cipher.CipherTab;
import io.wonderland.rh.cryptanalysis.CryptanalysisTab;
import io.wonderland.rh.digest.DigestTab;
import io.wonderland.rh.encoding.EncodingTab;
import java.security.Security;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class RabbitHoleApplication extends Application {

  public static void main(String[] args) {
    Security.addProvider(new BouncyCastleProvider());
    Security.addProvider(new AliceProvider());
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    TabPane tabPane = new TabPane();

    CipherTab cipherTab = new CipherTab("Crypto", "Cipher", primaryStage);
    CryptanalysisTab cryptanalysisTab = new CryptanalysisTab(primaryStage);
    DigestTab digestTab = new DigestTab("Digest", "MessageDigest", primaryStage);
    EncodingTab encodingTab = new EncodingTab(primaryStage);

    tabPane.getTabs().addAll(cipherTab, cryptanalysisTab, digestTab, encodingTab);

    Scene scene = new Scene(tabPane, 1124, 800);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Rabbit hole");
    primaryStage.show();
  }
}
