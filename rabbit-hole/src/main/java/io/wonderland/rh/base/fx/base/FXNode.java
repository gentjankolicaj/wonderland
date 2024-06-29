package io.wonderland.rh.base.fx.base;


import javafx.scene.Node;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import lombok.Getter;

@Getter
public class FXNode implements FXNodeMBean {

  public static final String DOMAIN = "io.wonderland.rh";
  private final Node node;
  private final ObjectName objectName;

  public FXNode(Node node) throws JMException {
    this.node = node;
    this.objectName = createName(node.getClass().getSimpleName(), toString());
  }


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

  public static FXNode create(Node node) {
    try {
      return new FXNode(node);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static ObjectName createName(String type, String name)
      throws MalformedObjectNameException {
    return new ObjectName(DOMAIN + ":type=" + type + ",name=" + name);
  }

  @Override
  public String toString() {
    //Identity Hash Code: Every object in Java has a unique identifier called its identity hash code.
    // You can obtain this hash code using the System.identityHashCode(Object obj) method.
    // While not a memory address, it provides a unique identifier for an object during its lifetime.
    return "" + System.identityHashCode(node);
  }

}
