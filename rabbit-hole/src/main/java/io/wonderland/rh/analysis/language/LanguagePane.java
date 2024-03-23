package io.wonderland.rh.analysis.language;


import io.wonderland.rh.analysis.AnalysisPane;
import io.wonderland.rq.type.Language;

import java.util.Map;

import javafx.scene.layout.BorderPane;

public class LanguagePane extends AnalysisPane<BorderPane> {

  public LanguagePane() {
    super(new BorderPane());
  }

  @Override
  public Map<String, Class<? extends AnalysisPane>> getChildren() {
    return Map.of(Language.AL.getName(), LanguageFrequencyPane.class,
        Language.EN.getName(), LanguageFrequencyPane.class,
        Language.DE.getName(), LanguageFrequencyPane.class);
  }

  @Override
  public String getKey() {
    return "Language";
  }


}
