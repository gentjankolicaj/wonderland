package io.wonderland.rh.base.common;

import java.util.function.Consumer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


public class CustomTreeItem<T> extends AbstractTreeItem<T> {
   private final Consumer<T> consumer;
  public CustomTreeItem(T value, Consumer<T> consumer){
    this.setValue(value);
     this.consumer=consumer;
  }

  @Override
  public ContextMenu getMenu() {
    ContextMenu contextMenu=new ContextMenu();
    MenuItem newWindow=new MenuItem("New window");
    newWindow.setOnAction(actionEvent -> consumer.accept(getValue()));
    contextMenu.getItems().add(newWindow);
    return contextMenu;
  }
}
