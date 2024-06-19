package io.wonderland.rh.base.fx;

import io.wonderland.rh.exception.BuildException;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public abstract class AbstractPane<I, C extends Pane> {

  protected C container = createContainer();

  public void preBuild(I i) throws BuildException {
  }

  protected abstract C createContainer();

}
