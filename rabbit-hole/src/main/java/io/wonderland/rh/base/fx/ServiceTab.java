package io.wonderland.rh.base.fx;

import javafx.scene.control.Tab;
import org.apache.commons.lang3.StringUtils;

public abstract class ServiceTab extends Tab {

  protected String title;
  protected String[] serviceTypes;

  protected ServiceTab(String title, String... serviceTypes) {
    this.title = title;
    this.serviceTypes = serviceTypes;
    this.setText(title);
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
