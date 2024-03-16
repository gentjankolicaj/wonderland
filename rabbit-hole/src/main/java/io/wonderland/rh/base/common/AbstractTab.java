package io.wonderland.rh.base.common;

import javafx.scene.control.Tab;
import javafx.stage.Stage;

public abstract class AbstractTab extends Tab {

  protected String title;
  protected Stage stage;

   protected AbstractTab(Stage stage, String title) {
    this.title = title;
    this.stage = stage;
    this.setText(title);
    this.setClosable(false);
  }

}
