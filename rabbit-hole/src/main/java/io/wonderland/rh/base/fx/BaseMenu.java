package io.wonderland.rh.base.fx;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.apache.commons.lang3.ArrayUtils;

public class BaseMenu extends Menu {


  public BaseMenu(String text, MenuItem... menuItems) {
    super(text);
    if (ArrayUtils.isNotEmpty(menuItems)) {
      getItems().addAll(menuItems);
    }
  }

}
