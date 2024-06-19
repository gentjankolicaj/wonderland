package io.wonderland.rh.keystore;

import io.wonderland.rh.base.fx.AbstractTab;

public class KeystoreTab extends AbstractTab {

  public KeystoreTab(String title) {
    super(title);
    KeystorePane keystorePane = new KeystorePane();
    this.setContent(keystorePane);
  }
}
