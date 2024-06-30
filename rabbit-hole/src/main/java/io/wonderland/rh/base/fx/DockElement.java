package io.wonderland.rh.base.fx;

import java.util.Optional;
import java.util.function.Supplier;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import lombok.Getter;

@Getter
public class DockElement extends Button {

  protected final TabPane tabPane;

  public DockElement(TabPane tabPane, ImageView icon, Supplier<? extends Tab> supplier) {
    this.tabPane = tabPane;
    this.setGraphic(icon);
    this.setListener(supplier);
  }

  private void setListener(Supplier<? extends Tab> supplier) {
    this.setOnMouseClicked(event -> addTab(supplier));
  }

  private void addTab(Supplier<? extends Tab> supplier) {
    Tab tab = supplier.get();
    Class clazz = tab.getClass();
    Optional<Tab> optional = tabPane.getTabs().stream()
        .filter(e -> e.getClass().isAssignableFrom(clazz)).findAny();
    if (optional.isEmpty()) {
      tabPane.getTabs().add(tab);
      this.setTooltip(new Tooltip(tab.getText()));
    }
  }

}
