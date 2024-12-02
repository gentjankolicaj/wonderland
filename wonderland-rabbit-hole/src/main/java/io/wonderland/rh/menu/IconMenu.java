package io.wonderland.rh.menu;

import java.io.IOException;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class IconMenu extends Menu {

  private final String url;
  private final MenuItem menuItem = new MenuItem("Icons are from Icons8, https://icons8.com");

  public IconMenu(String title, String url) {
    this.url = url;
    setText(title);
    build();
  }

  private void build() {
    menuItem.setOnAction(actionEvent -> openBrowserTab(getUrl()));
    this.getItems().add(menuItem);
  }

  private void openBrowserTab(String url) {
    try {
      new ProcessBuilder("x-www-browser", url).start();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}

