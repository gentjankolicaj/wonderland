package io.wonderland.rh.menu;

import io.wonderland.rh.base.fx.base.BaseMenu;
import io.wonderland.rh.base.fx.base.BaseMenuItem;
import java.io.File;
import java.util.function.Consumer;
import javafx.event.ActionEvent;

public class ServersMenu extends BaseMenu {

  protected static final String SERVER_DIR =
      System.getProperty("user.dir") + File.separatorChar + "rh_severs";

  public ServersMenu() {
    super("Servers", new GardenMenuItem(), new ManageConnectionsMenuItem());
  }

  static class GardenMenuItem extends BaseMenuItem {

    public GardenMenuItem() {
      super("Connect to garden...", getActionConsumer());
    }

    static Consumer<ActionEvent> getActionConsumer() {
      return actionEvent -> new GardenStage().show();
    }
  }


  static class ManageConnectionsMenuItem extends BaseMenuItem {

    public ManageConnectionsMenuItem() {
      super("Manage connections...", getActionConsumer());
    }

    static Consumer<ActionEvent> getActionConsumer() {
      return actionEvent -> new ManageConnectionsStage().show();
    }
  }

}
