package io.wonderland.rh.base.tree;

import java.util.function.Consumer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import lombok.Getter;

@Getter
public class BiTreeItem<T> extends AbstractTreeItem<T> {

  private final Consumer<T> consumer;
  private final Class clazz;

  public BiTreeItem(T value, Class clazz, Consumer<T> consumer) {
    this.setValue(value);
    this.clazz = clazz;
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
