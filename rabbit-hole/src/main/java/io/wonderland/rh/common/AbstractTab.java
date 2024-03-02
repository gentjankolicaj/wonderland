package io.wonderland.rh.common;

import javafx.scene.control.Tab;
import javafx.stage.Stage;

public abstract class AbstractTab extends Tab {

  protected String title;
  protected Stage stage;

  public AbstractTab(Stage stage, String title) {
    this.title = title;
    this.stage = stage;
    this.setText(title);
    this.setClosable(false);
  }


}
