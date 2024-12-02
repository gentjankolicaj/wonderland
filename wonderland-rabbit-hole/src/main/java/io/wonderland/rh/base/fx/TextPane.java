package io.wonderland.rh.base.fx;

import io.wonderland.rh.utils.GuiUtils;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextPane extends VBox {

  private static final int SPACING = 5;
  private String title;
  private Pane pane;
  private TextArea textArea;

  public TextPane(String title, TextArea textArea) {
    this.textArea = textArea;
    this.title = title;
    this.setSpacing(SPACING);
    VBox.setVgrow(textArea, Priority.ALWAYS);
    getChildren().addAll(GuiUtils.getTitle(title), textArea);
  }

  public TextPane(String title, Pane pane, TextArea textArea) {
    this.textArea = textArea;
    this.pane = pane;
    this.title = title;
    this.setSpacing(SPACING);
    VBox.setVgrow(textArea, Priority.ALWAYS);
    getChildren().addAll(GuiUtils.getTitle(title), pane, textArea);
  }

}
