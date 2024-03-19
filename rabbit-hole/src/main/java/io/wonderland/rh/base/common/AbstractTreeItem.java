package io.wonderland.rh.base.common;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

public abstract class AbstractTreeItem<T> extends TreeItem<T> {

  public abstract ContextMenu getMenu();

}
