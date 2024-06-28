package io.wonderland.rh.menu;


import javafx.scene.control.MenuBar;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MenuBarUtil {

  private MenuBarUtil() {
  }

  public static MenuBar getMenuBar() {
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus()
        .addAll(new ServersMenu(), new MonitorMenu(),
            new AboutMenu(), new IconMenu("Icons", "https://icons8.com"));
    return menuBar;
  }


}
