package io.wonderland.rh.base.fx;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfirmationDialog extends Alert {

  public ConfirmationDialog(String title, String header, String content) {
    super(AlertType.CONFIRMATION);
    build(title, header, content);
  }

  public ConfirmationDialog(String title, String header) {
    this(title, header, "");
  }

  private void build(String title, String header, String content) {
    setTitle(title);
    setHeaderText(header);
    setContentText(content);
    getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
  }

}
