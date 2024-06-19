package io.wonderland.rh.base.fx;


import io.wonderland.rh.GlobalConstants;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;


@SuppressWarnings("all")
public class OptionsDialog<T extends OptionsDialog.Option> extends Dialog<T> {

  private final VBox container = new VBox();
  private final T[] options;

  public OptionsDialog(int minWidth, int minHeight, T... options) {
    super();
    if (ArrayUtils.isEmpty(options)) {
      throw new IllegalArgumentException("Must provide at least one option.");
    }
    this.options = options;
    build();
  }

  protected void build() {
    this.container.setAlignment(Pos.CENTER);
    for (T option : options) {
      var button = new Button(option.getDisplayText());
      button.setOnAction(Event::consume);
      this.container.getChildren().add(button);
    }
    resize();
    getDialogPane().setContent(container);
  }

  private void resize() {
    getDialogPane().setMinSize(GlobalConstants.DIALOG_SIZE[0], GlobalConstants.DIALOG_SIZE[1]);
  }

  public interface Option<T> {

    String getDisplayText();

    T getValue();
  }

}
