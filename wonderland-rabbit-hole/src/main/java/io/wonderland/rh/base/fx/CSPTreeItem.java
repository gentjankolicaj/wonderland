package io.wonderland.rh.base.fx;

import javafx.scene.control.TreeItem;
import lombok.Getter;

@Getter
public class CSPTreeItem extends TreeItem<String> {

  private final String versionStr;
  private final String name;

  public CSPTreeItem(String name, String versionStr) {
    super(name + "-" + versionStr);
    this.name = name;
    this.versionStr = versionStr;
  }
}
