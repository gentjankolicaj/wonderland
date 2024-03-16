package io.wonderland.rh.base.common;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class LabelPane extends Pane {
  public LabelPane(String text) {
    this.getChildren().add(new Label(text));
  }
}
