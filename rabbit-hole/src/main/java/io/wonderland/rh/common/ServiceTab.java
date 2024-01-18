package io.wonderland.rh.common;

import io.wonderland.rh.exception.ServiceException;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public abstract class ServiceTab<T> extends Tab {

  protected String title;
  protected String serviceType;
  protected Stage stage;

  public ServiceTab(String title, String serviceType, Stage stage) {
    this.title = title;
    this.serviceType = serviceType;
    this.stage = stage;
    this.setText(title);
    this.setClosable(false);
  }

  protected abstract T getDefaultService();

  protected abstract T getService(String serviceName) throws ServiceException;

  protected abstract boolean isValidServiceName(String name);

}
