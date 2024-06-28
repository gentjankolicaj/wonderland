package io.wonderland.rh.base.fx.base;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TabPane;

public class BaseTabPane extends TabPane implements BaseTabPaneMBean {

  @Override
  public List<FXNode> getChildrenFXNode() {
    return getChildren().stream().map(FXNode::new).collect(Collectors.toList());
  }

}
