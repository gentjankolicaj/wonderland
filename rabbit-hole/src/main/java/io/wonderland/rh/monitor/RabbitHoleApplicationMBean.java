package io.wonderland.rh.monitor;

import java.util.List;

public interface RabbitHoleApplicationMBean extends BaseMBean {

  List<TabMBean> getTabs();

}
