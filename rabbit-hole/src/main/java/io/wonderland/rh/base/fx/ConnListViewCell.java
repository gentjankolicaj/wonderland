package io.wonderland.rh.base.fx;

import io.wonderland.rh.utils.GuiUtils;
import java.nio.file.Path;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;

@Getter
public class ConnListViewCell extends GridPane {

  public ConnListViewCell(Path path) {
    this.build(path);
  }

  private void build(Path path) {
    Label name = new Label(path.getName(path.getNameCount() - 1).toString());
    disconnectedIcon();
    add(name, 1, 0);
    setHgap(5);
  }

  public void connectedIcon() {
    Label label = new Label();
    label.setGraphic(GuiUtils.getIconClasspath("/icons/conn/icons8-connected-20.png"));
    add(label, 0, 0);
    requestLayout();
  }

  public void disconnectedIcon() {
    Label label = new Label();
    label.setGraphic(GuiUtils.getIconClasspath("/icons/conn/icons8-disconnected-20.png"));
    add(label, 0, 0);
  }
}
