package io.wonderland.rh.base.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class LabelPane extends Pane {
  public LabelPane(String text) {
    this.getChildren().add(new Label(text));
  }
}
