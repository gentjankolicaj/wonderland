package io.wonderland.rh.common;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.commons.lang3.ArrayUtils;

public class VToggleGroupPane extends VBox {

  private final ToggleGroup toggleGroup = new ToggleGroup();

  public VToggleGroupPane(String title, String... buttonTexts) {
    if (ArrayUtils.isNotEmpty(buttonTexts)) {
      List<RadioButton> buttonList = new ArrayList<>();
      for (String buttonText : buttonTexts) {
        buttonList.add(new RadioButton(buttonText));
      }
      buttonList.forEach(b -> b.setToggleGroup(toggleGroup));
      this.setSpacing(13);
      this.getChildren().add(getTitle(title));
      this.getChildren().addAll(buttonList);
    } else {
      this.setSpacing(13);
      this.getChildren().addAll(getTitle(title));
    }
  }

  public RadioButton getSelectedRadioButton() {
    return (RadioButton) toggleGroup.getSelectedToggle();
  }

  public ToggleGroup getToggleGroup() {
    return this.toggleGroup;
  }

  private Label getTitle(String title) {
    Label label = new Label(title);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }


}
