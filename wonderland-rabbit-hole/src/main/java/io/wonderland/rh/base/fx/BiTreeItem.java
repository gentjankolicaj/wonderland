package io.wonderland.rh.base.fx;

import java.util.function.Consumer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import lombok.Getter;

@Getter
public class BiTreeItem<T, N> extends AbstractTreeItem<T> {

  private final Consumer<T> consumer;
  private final N node;

  public BiTreeItem(T value, N node, Consumer<T> consumer) {
    this.setValue(value);
    this.node = node;
    this.consumer = consumer;
  }

  @Override
  public ContextMenu getMenu() {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem newWindow = new MenuItem("New window");
    newWindow.setOnAction(actionEvent -> consumer.accept(getValue()));
    contextMenu.getItems().add(newWindow);
    return contextMenu;
  }
}
