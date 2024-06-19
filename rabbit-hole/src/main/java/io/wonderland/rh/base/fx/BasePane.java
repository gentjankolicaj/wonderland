package io.wonderland.rh.base.fx;


import javafx.scene.layout.Pane;

public class BasePane<I, C extends Pane, A> extends AbstractPane<I, C> {

  public void sceneBuild(double sceneWidth, double sceneHeight, A... args) {
    //no need here because this class is inherited
  }

  @Override
  protected C createContainer() {
    return null;
  }


}
