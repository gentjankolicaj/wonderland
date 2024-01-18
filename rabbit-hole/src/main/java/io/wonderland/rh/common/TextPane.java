package io.wonderland.rh.common;

import java.util.Objects;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextPane extends BorderPane {

  private TextArea textArea;
  private String label;

  public TextPane(String label, TextArea textArea) {
    this.textArea = textArea;
    this.label = label;

    //key scroll pane
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(textArea);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    setTop(createTitle(label));
    setCenter(scrollPane);
    setBorder(new Border(new BorderStroke(Color.BLACK,
        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
  }

  public TextPane(String label, TextArea textArea, Pane leftPane, Pane rightPane) {
    this.textArea = textArea;
    this.label = label;

    //key scroll pane
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(textArea);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    setTop(createTitle(label));
    setCenter(scrollPane);

    if (Objects.nonNull(leftPane)) {
      setLeft(leftPane);
    }
    if (Objects.nonNull(rightPane)) {
      setRight(rightPane);
    }
    setBorder(new Border(new BorderStroke(Color.BLACK,
        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
  }

  private Label createTitle(String title) {
    Label label = new Label(title);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }

}
