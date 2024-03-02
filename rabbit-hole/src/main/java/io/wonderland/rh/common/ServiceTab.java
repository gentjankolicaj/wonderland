package io.wonderland.rh.common;

import io.wonderland.rh.exception.ServiceException;
import java.util.Optional;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public abstract class ServiceTab<T> extends Tab {

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

  protected  Optional<T> getDefaultService(){
    return Optional.empty();
  }

  protected  T getService(String serviceName) throws ServiceException{
    throw new UnsupportedOperationException("Method not implemented.");
  }

  protected boolean isValidServiceName(String name){
    return true;
  }


}
