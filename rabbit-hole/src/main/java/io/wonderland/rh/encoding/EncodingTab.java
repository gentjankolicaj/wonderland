package io.wonderland.rh.encoding;

import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class EncodingTab extends Tab {

  private final Stage primaryStage;

  public EncodingTab(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.setText("Encoding");
    this.setClosable(false);
  }

}
