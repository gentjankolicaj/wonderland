package io.wonderland.rh.base.fx.base;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class BaseTab extends AbstractTab implements BaseTabMBean {

  protected String[] serviceTypes;

  protected BaseTab(String title, String... serviceTypes) {
    super(title);
    this.serviceTypes = serviceTypes;
  }

  protected boolean isValidServiceName(String name) {
    if (StringUtils.isEmpty(name)) {
      return false;
    } else {
      return !(name.contains(".") || name.contains("OID"));
    }
  }

  protected String parseCspName(String rawName) {
    return rawName.substring(0, rawName.indexOf("-"));
  }

  @Override
  public List<FXNode> getChildrenFXNode() {
    return List.of(new FXNode(getContent()));
  }

}
