package io.wonderland.rh.base.fx.base;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NodeDynamicMBean implements DynamicMBean {

  public static final String DOMAIN = "io.wonderland.rh";

  private final Map<String, Object> attributes = new HashMap<>();
  private final Map<String, Class<?>> attributeTypes = new HashMap<>();
  private final Map<String, Method> operations = new HashMap<>();
  private final ObjectName objectName;
  private final Node node;


  public NodeDynamicMBean(Node node) throws JMException {
    this.node = node;
    this.objectName = createName(node.getClass().getSimpleName(), toString());
    this.createAttr(node);
    this.createOps(node);
  }

  public static NodeDynamicMBean create(Node node) {
    try {
      return new NodeDynamicMBean(node);
    } catch (Exception e) {
      log.error("", e);
      return null;
    }
  }

  public static ObjectName createName(String type, String name)
      throws MalformedObjectNameException {
    return new ObjectName(DOMAIN + ":type=" + type + ",name=" + name);
  }

  private void createAttr(Node node) {
    Field[] fields = node.getClass().getDeclaredFields();
    for (Field field : fields) {
      //check if field is public to avoid IllegalAccessException
      if (Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
        String fieldName = field.getName();
        try {
          attributes.put(fieldName, field.get(node));
          attributeTypes.put(fieldName, field.getType());
        } catch (Exception e) {
          //do nothing currently, to be looked into the future regarding external module field access.
        }
      }
    }
  }

  private void createOps(Node node) {
    Method[] methods = node.getClass().getDeclaredMethods();
    for (Method method : methods) {
      if (Modifier.isPublic(method.getModifiers())) {
        operations.put(method.getName(), method);
      }
    }
  }

  @Override
  public Object getAttribute(String attribute) throws AttributeNotFoundException {
    if (attributes.containsKey(attribute)) {
      return attributes.get(attribute);
    } else {
      throw new AttributeNotFoundException("No such attribute: " + attribute);
    }

  }

  @Override
  public void setAttribute(Attribute attribute) throws AttributeNotFoundException {
    if (attributes.containsKey(attribute.getName())) {
      attributes.put(attribute.getName(), attribute.getValue());
    } else {
      throw new AttributeNotFoundException("No such attribute: " + attribute.getName());
    }
  }

  @Override
  public AttributeList getAttributes(String[] attributeNames) {
    AttributeList list = new AttributeList();
    for (String attributeName : attributeNames) {
      try {
        list.add(new Attribute(attributeName, getAttribute(attributeName)));
      } catch (AttributeNotFoundException e) {
        log.error("", e);
      }
    }
    return list;
  }

  @Override
  public AttributeList setAttributes(AttributeList attributeList) {
    AttributeList list = new AttributeList();
    for (Object obj : attributeList) {
      Attribute attribute = (Attribute) obj;
      try {
        setAttribute(attribute);
        list.add(new Attribute(attribute.getName(), attribute.getValue()));
      } catch (Exception e) {
        log.error("", e);
      }
    }
    return list;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature)
      throws ReflectionException {
    if (operations.containsKey(actionName)) {
      Method method = operations.get(actionName);
      try {
        return method.invoke(node, params);
      } catch (Exception e) {
        return null;
      }
    } else {
      throw new ReflectionException(new NoSuchMethodException(actionName));
    }
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    MBeanAttributeInfo[] attrInfos = attributes.keySet().stream()
        .map(o -> new MBeanAttributeInfo(o,
            attributeTypes.get(o).getName(), o, true, true, false))
        .toArray(MBeanAttributeInfo[]::new);

    MBeanOperationInfo[] operationInfos = operations.entrySet().stream()
        .map(e -> new MBeanOperationInfo(e.getKey(), e.getValue()))
        .toArray(MBeanOperationInfo[]::new);

    return new MBeanInfo(this.getClass().getName(), "Runtime Dynamic MBean",
        attrInfos, null, operationInfos, null);
  }

  @Override
  public String toString() {
    //Identity Hash Code: Every object in Java has a unique identifier called its identity hash code.
    // You can obtain this hash code using the System.identityHashCode(Object obj) method.
    // While not a memory address, it provides a unique identifier for an object during its lifetime.
    return "" + System.identityHashCode(node);
  }


}
