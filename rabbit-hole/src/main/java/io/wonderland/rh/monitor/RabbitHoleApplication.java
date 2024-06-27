package io.wonderland.rh.monitor;

import java.util.List;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import lombok.Getter;

@Getter
public class RabbitHoleApplication implements RabbitHoleApplicationMBean {

  private final ObjectName objectName;

  public RabbitHoleApplication() throws MalformedObjectNameException {
    this.objectName = new ObjectName(createName("type", this.getClass().getSimpleName()));
  }

  @Override
  public List<TabMBean> getTabs() {
    return List.of();
  }

}
