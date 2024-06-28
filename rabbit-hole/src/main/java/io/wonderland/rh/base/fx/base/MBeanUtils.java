package io.wonderland.rh.base.fx.base;

import io.wonderland.rh.monitor.JMXBase;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.commons.lang3.StringUtils;

public class MBeanUtils {

  private static int BEAN_COUNTER = 0;

  public static ObjectName createName(FXNode fxNode) throws MalformedObjectNameException {
    String name;
    if (StringUtils.isEmpty(fxNode.getId())) {
      name = StringUtils.isEmpty(fxNode.getAccessibleText()) ?
          fxNode.getNode().getClass().getSimpleName() + "-" + BEAN_COUNTER
          : fxNode.getAccessibleText();
    } else {
      name = fxNode.getId();
    }
    BEAN_COUNTER++;
    return new ObjectName(
        JMXBase.DOMAIN + ":type=" + fxNode.getClass().getSimpleName() + ",name=" + name);
  }

  public static ObjectName createName(ParentMBean mBean) throws MalformedObjectNameException {
    String name = mBean.getClass().getSimpleName() + "-" + BEAN_COUNTER;
    BEAN_COUNTER++;
    return new ObjectName(JMXBase.DOMAIN + ":type=" + name);
  }

}
