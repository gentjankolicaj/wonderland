package io.wonderland.rh.analysis;

import java.util.Map;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public abstract class AbstractPane<C extends Pane> {

  protected final C container;

  protected AbstractPane(C container) {
    this.container = container;
  }

  public abstract String getKey();

  public abstract Map<String, Class<? extends AnalysisPane>> getChildren();

}
