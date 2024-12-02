package io.wonderland.rh.base.fx;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ExceptionDialog extends Alert {

  public ExceptionDialog(String title, String header, String content, Throwable t) {
    super(AlertType.ERROR);
    build(title, header, content, t);
  }

  public ExceptionDialog(Exception e) {
    this("Exception", "An exception occurred.", e.getMessage(), e);
  }

  public ExceptionDialog(Throwable t) {
    this("Exception", "An exception occurred.", t.getMessage(), t);
  }

  private void build(String title, String header, String content, Throwable t) {
    setTitle(title);
    setHeaderText(header);
    setContentText(content);

    Label stackTraceLbl = new Label();

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    t.printStackTrace(printWriter);

    TextArea textArea = new TextArea(stringWriter.toString());
    textArea.setEditable(false);
    textArea.setWrapText(true);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    GridPane expContent = new GridPane();
    expContent.setMaxWidth(Double.MAX_VALUE);
    expContent.add(stackTraceLbl, 0, 0);
    expContent.add(textArea, 0, 1);

    // Set expandable Exception into the dialog pane.
    getDialogPane().setExpandableContent(expContent);
  }
}
