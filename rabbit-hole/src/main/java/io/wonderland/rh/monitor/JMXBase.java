package io.wonderland.rh.monitor;

import io.wonderland.rh.base.fx.base.FXNode;
import io.wonderland.rh.base.fx.base.FXParentMBean;
import io.wonderland.rh.base.fx.base.MBeanUtils;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public final class JMXBase {

  public static final String DOMAIN = "io.wonderland.rh";

  private static final List<Object> MBEANS = new CopyOnWriteArrayList<>();

  private JMXBase() {
  }


  public static void start() {
    try {
      MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

      //management beans
      for (Object mBean : MBEANS) {
        if (mBean instanceof FXParentMBean) {
          FXParentMBean FXParentMBean = (FXParentMBean) mBean;
          mBeanServer.registerMBean(FXParentMBean, MBeanUtils.createName(FXParentMBean));

          //recursive register of children
          List<FXNode> children = FXParentMBean.getChildrenFXNode();
          for (FXNode fxNode : children) {
            fxNode.registerBean(mBeanServer);
          }
        } else if (mBean instanceof FXNode) {
          FXNode fxNode = (FXNode) mBean;
          fxNode.registerBean(mBeanServer);
        } else {
          mBeanServer.registerMBean(mBean, createObjectName(mBean.getClass().getSimpleName()));
        }
      }
    } catch (Exception e) {
      log.error("", e);
    }

  }

  private static ObjectName createObjectName(String name)
      throws MalformedObjectNameException {
    return new ObjectName(DOMAIN + ":type=" + name);

  }


  public static void addParentBean(Object... beans) {
    if (ArrayUtils.isEmpty(beans)) {
      throw new IllegalArgumentException("Can't register empty beans");
    }
    Collections.addAll(MBEANS, beans);
  }


}
