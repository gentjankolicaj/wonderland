package io.wonderland.rh.base.tab;

import javafx.scene.control.Tab;
import javafx.stage.Stage;

public abstract class ServiceTab extends Tab {

  protected String title;
  protected String[] serviceTypes;
  protected Stage stage;

  public ServiceTab(Stage stage, String title, String... serviceTypes) {
    this.title = title;
    this.serviceTypes = serviceTypes;
    this.stage = stage;
    this.setText(title);
    this.setClosable(false);
  }

  protected boolean isValidServiceName(String name){
    return true;
  }

}
