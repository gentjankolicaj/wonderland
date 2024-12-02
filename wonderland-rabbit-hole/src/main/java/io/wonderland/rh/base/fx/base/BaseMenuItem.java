package io.wonderland.rh.base.fx.base;


import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;

public class BaseMenuItem extends MenuItem {

  public BaseMenuItem(String text, Consumer<ActionEvent> actionEventConsumer) {
    super(text);
    this.setOnAction(actionEventConsumer::accept);
  }

}
