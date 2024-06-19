package io.wonderland.rh.base.fx;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.analysis.AnalysisTab;
import io.wonderland.rh.cipher.CipherTab;
import io.wonderland.rh.hash.HashTab;
import io.wonderland.rh.keygen.KeygenTab;
import io.wonderland.rh.keystore.KeystoreTab;
import io.wonderland.rh.mac.MacTab;
import io.wonderland.rh.misc.MiscTab;
import io.wonderland.rh.utils.GuiUtils;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class Dock extends VBox {


  public Dock(TabPane tabPane) {
    this.getChildren()
        .addAll(new DockElement(tabPane, GuiUtils.getIconClasspath("/icons/icons8-encrypt-50.png"),
                () -> new CipherTab(" Cipher ", "Cipher")),
            new DockElement(tabPane, GuiUtils.getIconClasspath("/icons/icons8-key-security-50.png"),
                () -> new KeygenTab(" Keygen ", "KeyGenerator", "KeyPairGenerator")),
            new DockElement(tabPane, GuiUtils.getIconClasspath("/icons/icons8-hash-50.png"),
                () -> new HashTab("  Hash  ", "MessageDigest")),
            new DockElement(tabPane,
                GuiUtils.getIconClasspath("/icons/icons8-authentication-50.png"),
                () -> new MacTab("  MAC  ", "Mac")),
            new DockElement(tabPane, GuiUtils.getIconClasspath("/icons/icons8-keystore-50.png"),
                () -> new KeystoreTab(" Keystore ")),
            new DockElement(tabPane, GuiUtils.getIconClasspath("/icons/icons8-analysis-50.png"),
                () -> new AnalysisTab(" Analysis ")),
            new DockElement(tabPane, GuiUtils.getIconClasspath("/icons/icons8-tools-50.png"),
                () -> new MiscTab(" Misc ")));
    this.setSpacing(GlobalConstants.SPACING);
  }

}
