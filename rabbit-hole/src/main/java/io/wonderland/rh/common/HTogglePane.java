package io.wonderland.rh.common;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

public class HTogglePane<T extends ToggleButton> extends HBox {

  private final ToggleGroup toggleGroup = new ToggleGroup();

  public HTogglePane(String title, Function<String, T> fun, Map<String, Runnable> buttonMap) {
    if (MapUtils.isNotEmpty(buttonMap)) {
      List<ToggleButton> buttonList = new ArrayList<>();
      for (String buttonText : buttonMap.keySet()) {
        ToggleButton toggle = fun.apply(buttonText);
        toggle.setUserData(buttonText);
        buttonList.add(toggle);
      }
      buttonList.forEach(b -> b.setToggleGroup(toggleGroup));
      this.setSpacing(13);
      this.getChildren().add(getTitle(title));
      this.getChildren().addAll(buttonList);

      toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.isSelected()) {
          buttonMap.get(newValue.getUserData()).run();
        }
      });
    } else {
      this.setSpacing(13);
      this.getChildren().addAll(getTitle(title));
    }
  }

  public T getSelectedToggle() {
    return (T) toggleGroup.getSelectedToggle();
  }

  public ToggleGroup getToggleGroup() {
    return this.toggleGroup;
  }

  public void selectToggle(String buttonText) {
    for (Toggle toggle : toggleGroup.getToggles()) {
      if (((String) toggle.getUserData()).equalsIgnoreCase(buttonText)) {
        toggle.setSelected(true);
        break;
      }
    }
  }

  private Label getTitle(String title) {
    Label label = new Label(title);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }


}
