package io.wonderland.rh.base.fx.base;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.layout.HBox;

public class BaseHBox extends HBox implements BaseHBoxMBean {

  @Override
  public List<FXNode> getChildrenFXNode() {
    return getChildren().stream().map(FXNode::new).collect(Collectors.toList());
  }

}
