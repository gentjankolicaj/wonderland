package io.wonderland.rh.base.common;

import io.wonderland.rh.utils.LabelUtils;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextPane extends VBox {

  private String title;
  private Pane pane;
  private TextArea textArea;
  public TextPane(String title, TextArea textArea) {
    this.textArea = textArea;
    this.title=title;
    this.setSpacing(10);
    VBox.setVgrow(textArea, Priority.ALWAYS);
    getChildren().addAll(LabelUtils.getTitle(title),textArea);
  }

  public TextPane(String title, Pane pane, TextArea textArea) {
    this.textArea = textArea;
    this.pane=pane;
    this.title=title;
    this.setSpacing(10);
    VBox.setVgrow(textArea, Priority.ALWAYS);
    getChildren().addAll(LabelUtils.getTitle(title),pane,textArea);
  }

}
