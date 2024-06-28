package io.wonderland.rh.base.fx.base;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.Parent;
import javax.management.JMException;
import javax.management.MBeanServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FXNode implements FXNodeMBean {

  private final Node node;

  @Override
  public String getId() {
    return node.getId();
  }

  @Override
  public String getAccessibleText() {
    return node.getAccessibleText();
  }

  @Override
  public double getLayoutX() {
    return node.getLayoutX();
  }

  @Override
  public void setLayoutX(double layoutX) {
    this.node.setLayoutX(layoutX);
  }

  @Override
  public double getLayoutY() {
    return this.node.getLayoutY();
  }

  @Override
  public void setLayoutY(double layoutY) {
    this.node.setLayoutY(layoutY);
  }

  /**
   * Recursive registering of child nodes
   *
   * @param mBeanServer
   * @throws JMException
   */
  public void registerBean(MBeanServer mBeanServer) throws JMException {
    if (node instanceof Parent) {
      Parent parent = (Parent) node;
      List<FXNode> children = parent.getChildrenUnmodifiable().stream().map(FXNode::new).collect(
          Collectors.toList());
      for (FXNode child : children) {
        child.registerBean(mBeanServer);
      }
    } else {
      mBeanServer.registerMBean(this, MBeanUtils.createName(this));
    }
  }

}
