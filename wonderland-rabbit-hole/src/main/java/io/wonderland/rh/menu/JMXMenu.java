package io.wonderland.rh.menu;

import io.wonderland.rh.base.fx.base.BaseMenu;
import io.wonderland.rh.base.fx.base.BaseMenuItem;
import io.wonderland.rh.monitor.JConsoleBase;
import io.wonderland.rh.monitor.JMXBase;
import java.util.function.Consumer;
import javafx.event.ActionEvent;

public class JMXMenu extends BaseMenu {

  public static boolean monitoring = false;

  public JMXMenu() {
    super("Jmx", new JConsoleItem());
  }


  static class JConsoleItem extends BaseMenuItem {

    public JConsoleItem() {
      super("JConsole", getActionConsumer());
    }

    static Consumer<ActionEvent> getActionConsumer() {
      return actionEvent -> {
        if (!monitoring) {
          JMXBase.start();
          JConsoleBase.start(1, ProcessHandle.current().pid());
          monitoring = true;
        }
      };
    }
  }


}
