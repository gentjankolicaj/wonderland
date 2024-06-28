package io.wonderland.rh.base.fx.base;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class BaseStage extends Stage {

  public BaseStage(String title, boolean resizable) {
    super();
    this.setTitle(title);
    this.setResizable(resizable);
    this.initModality(Modality.APPLICATION_MODAL);
  }

}