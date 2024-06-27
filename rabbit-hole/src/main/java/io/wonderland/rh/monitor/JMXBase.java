package io.wonderland.rh.monitor;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JMXBase {

  private JMXBase() {
  }


  public static void start() {
    try {
      MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

      RabbitHoleApplication rabbitHoleMBean = new RabbitHoleApplication();
      mBeanServer.registerMBean(rabbitHoleMBean, rabbitHoleMBean.getObjectName());

    } catch (Exception e) {
      log.error("", e);
    }

  }


}
