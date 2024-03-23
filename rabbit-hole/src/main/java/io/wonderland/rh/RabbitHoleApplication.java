package io.wonderland.rh;


import io.wonderland.alice.jca.AliceProvider;
import io.wonderland.rh.cipher.CipherTab;
import io.wonderland.rh.analysis.AnalysisTab;
import io.wonderland.rh.hash.HashTab;
import io.wonderland.rh.mac.MacTab;
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
  public void start(Stage stage) {
    TabPane tabPane = new TabPane();

    CipherTab cipherTab = new CipherTab(stage,"Crypto","Cipher");
    AnalysisTab analysisTab = new AnalysisTab(stage, "Analysis");
    HashTab digestTab = new HashTab(stage,"Hash", "MessageDigest");
    MacTab macTab=new MacTab(stage,"MAC","Mac");
    KeygenTab keygenTab=new KeygenTab(stage,"Keygen","KeyGenerator","KeyPairGenerator");
    MiscTab miscTab = new MiscTab(stage,"Misc");

    tabPane.getTabs().addAll(cipherTab, digestTab, macTab, keygenTab, analysisTab, miscTab);

    Scene scene = new Scene(tabPane, 1100, 800);
    stage.setScene(scene);
    stage.setTitle("Rabbit hole");
    stage.show();
  }

  private static void addSecurityProviders(){
    Security.addProvider(new BouncyCastleProvider());
    Security.addProvider(new BouncyCastlePQCProvider());
    Security.addProvider(new OpenSSLProvider());
    Security.addProvider(new AliceProvider());
  }
}
