package io.wonderland.rh.analysis;


import io.wonderland.rh.analysis.ciphertext.CiphertextPane;
import io.wonderland.rh.analysis.language.LanguagePane;
import io.wonderland.rh.utils.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
public class AnalysisPane<C extends Pane> extends AbstractPane<C> {

  protected AnalysisPane(C container, double width, double height) {
    super(container);
  }

  protected AnalysisPane(C container) {
    super(container);
  }

  @Override
  public String getKey() {
    return "Analysis";
  }

  @Override
  public Map<String, Class<? extends AnalysisPane>> getChildren() {
    return Map.of("Language", LanguagePane.class, "Ciphertext", CiphertextPane.class);
  }

  public static AnalysisPane empty() {
    return new AnalysisPane(new VBox());
  }

  public static Map<String, Class<? extends AnalysisPane>> getClassChildren(Class<? extends AnalysisPane> clazz) {
    try {
      AnalysisPane analysisPane = clazz.getDeclaredConstructor().newInstance(null);
      return (Map<String, Class<? extends AnalysisPane>>) clazz.getMethod("getChildren").invoke(analysisPane);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return MapUtils.EMPTY_SORTED_MAP;
  }

  public static AnalysisPane newInstance(String value, Class<? extends AnalysisPane> clazz) {
    try {
      return clazz.getConstructor(value.getClass()).newInstance(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
