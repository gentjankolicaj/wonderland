package io.wonderland.rh.base.fx.base;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.layout.VBox;

public class BaseVBox extends VBox implements BaseVBoxMBean {

  @Override
  public List<FXNode> getChildrenFXNode() {
    return getChildren().stream().map(FXNode::new).collect(Collectors.toList());
  }
}
