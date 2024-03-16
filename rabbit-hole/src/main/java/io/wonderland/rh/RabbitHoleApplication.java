package io.wonderland.rh;


import io.wonderland.alice.jca.AliceProvider;
import io.wonderland.rh.cipher.CipherTab;
import io.wonderland.rh.cryptanalysis.CryptanalysisTab;
import io.wonderland.rh.hash.HashTab;
import io.wonderland.rh.misc.MiscTab;
import io.wonderland.rh.keygen.KeygenTab;
import java.security.Security;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.conscrypt.OpenSSLProvider;


public class RabbitHoleApplication extends Application {

  public static void main(String[] args) {
    addSecurityProviders();
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    TabPane tabPane = new TabPane();

    CipherTab cipherTab = new CipherTab(primaryStage,"Crypto","Cipher");
    CryptanalysisTab cryptanalysisTab = new CryptanalysisTab(primaryStage);
    HashTab digestTab = new HashTab(primaryStage,"Hash", "MessageDigest");
    KeygenTab keygenTab=new KeygenTab(primaryStage,"Keygen","KeyGenerator","KeyPairGenerator");
    MiscTab miscTab = new MiscTab(primaryStage,"Misc");

    tabPane.getTabs().addAll(cipherTab,  digestTab,keygenTab,cryptanalysisTab, miscTab);

    Scene scene = new Scene(tabPane, 1124, 800);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Rabbit hole");
    primaryStage.show();
  }

  private static void addSecurityProviders(){
    Security.addProvider(new BouncyCastleProvider());
    Security.addProvider(new BouncyCastlePQCProvider());
    Security.addProvider(new OpenSSLProvider());
    Security.addProvider(new AliceProvider());
  }
}
