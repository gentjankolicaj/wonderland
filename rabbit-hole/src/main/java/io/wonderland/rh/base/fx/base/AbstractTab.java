package io.wonderland.rh.base.fx.base;

import javafx.scene.control.Tab;

public abstract class AbstractTab extends Tab {

  protected String title;

  protected AbstractTab(String title) {
    this.title = title;
    this.setText(title);
  }

}
