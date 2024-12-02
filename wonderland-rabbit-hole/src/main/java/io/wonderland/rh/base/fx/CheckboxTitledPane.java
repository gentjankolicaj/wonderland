package io.wonderland.rh.base.fx;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@Slf4j
public class CheckboxTitledPane extends TitledPane {

  private final CheckBox checkBox;

  public CheckboxTitledPane(String title) {
    this.checkBox = new CheckBox(title);
    this.checkBox.selectedProperty().addListener(checkboxListener());
    this.setGraphic(new BorderPane(checkBox));
  }

  protected ChangeListener<Boolean> checkboxListener() {
    return (observable, oldValue, newValue) -> {
      if (Boolean.TRUE.equals(newValue)) {
        log.info("Checkbox clicked");
      }
    };
  }

  protected void selectCheckbox(boolean value) {
    checkBox.setSelected(value);
  }

}
