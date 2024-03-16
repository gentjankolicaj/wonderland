package io.wonderland.rh.base.common;


import io.wonderland.rh.utils.LabelUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.apache.commons.collections4.MapUtils;

@Getter
public class HToggleBox<T extends ToggleButton> extends HBox {

  private final ToggleGroup toggleGroup = new ToggleGroup();
  private final Map<String, Runnable> onToggleSelected;

  public HToggleBox(String title,int spacing, Function<String, T> onInit, Map<String, Runnable> onToggleSelected) {
    this.onToggleSelected = onToggleSelected;
    if (MapUtils.isNotEmpty(onToggleSelected)) {
      List<ToggleButton> buttonList = new ArrayList<>();
      for (Entry<String,Runnable> entry:onToggleSelected.entrySet()) {
        ToggleButton toggle = onInit.apply(entry.getKey());
        toggle.setUserData(entry.getKey());
        buttonList.add(toggle);
      }
      buttonList.forEach(b -> b.setToggleGroup(toggleGroup));
      this.getChildren().add(LabelUtils.getTitle(title));
      this.getChildren().addAll(buttonList);

      this.toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.isSelected()) {
          Runnable runnable = newValue.getUserData() != null ? onToggleSelected.get(newValue.getUserData()) : null;
          if (Objects.nonNull(runnable)) {
            runnable.run();
          }
        }
      });
    } else {
      this.getChildren().addAll(new Label(title));
    }
    this.setSpacing(spacing);
  }

  public T getSelectedToggle() {
    return (T) toggleGroup.getSelectedToggle();
  }

  public void selectToggle(String buttonText) {
    for (Toggle toggle : toggleGroup.getToggles()) {
      if (((String) toggle.getUserData()).equalsIgnoreCase(buttonText)) {
        toggle.setSelected(true);
        break;
      }
    }
  }
}
