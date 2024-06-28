package io.wonderland.rh.base.fx;

import org.apache.commons.lang3.StringUtils;

public abstract class ServiceTab extends AbstractTab {

  protected String[] serviceTypes;

  protected ServiceTab(String title, String... serviceTypes) {
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

}
