package io.wonderland.rh.analysis.ciphertext;

import io.wonderland.rh.analysis.AnalysisPane;
import io.wonderland.rh.base.pane.HToggleBox;
import io.wonderland.rh.base.pane.TextPane;
import io.wonderland.rq.type.Language;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

@Slf4j
public class CiphertextPane extends AnalysisPane<BorderPane> {

  public static final String CIPHERTEXT = "Ciphertext";
  private final BodyPane bodyPane = new BodyPane();
  private final HeaderPane headerPane = new HeaderPane(bodyPane);

  public CiphertextPane() {
    super(new BorderPane());
    build();
  }

  private void build() {
    container.setTop(this.headerPane);
    container.setCenter(this.bodyPane);
  }

  @Override
  public String getKey() {
    return CIPHERTEXT;
  }

  @Override
  public Map<String, Class<? extends AnalysisPane>> getChildren() {
    return MapUtils.EMPTY_SORTED_MAP;
  }


  class HeaderPane extends VBox {

    private final TextArea ciphertextArea = new TextArea();
    private final BodyPane bodyPane;
    private final ComboBox<String> languageComboBox = new ComboBox<>(FXCollections.observableArrayList(
        Arrays.stream(Language.values()).map(e -> e.getName()).collect(Collectors.toList())));
    private final Button freqAnalysisBtn = new Button("Freq analysis");
    private final Button mppAnalysisBtn = new Button("MPP analysis");
    private final Button icAnalysisBtn = new Button("IC analysis");

    private final HToggleBox<RadioButton> langGraphPane = new HToggleBox<>("Character graph : ", 10,s->new RadioButton(s),
        Map.of("monograph",()->{}, "digraph",()->{},
        "trigraph", ()->{},"quadgraph",()->{}));
    private final TextPane ciphertextPane = new TextPane("Ciphertext", ciphertextArea);

    public HeaderPane(BodyPane bodyPane) {
      this.bodyPane = bodyPane;
      init(bodyPane);
    }

    private void init(BodyPane bodyPane) {
      this.languageComboBox.getSelectionModel().select(Language.EN.getName());
      this.freqAnalysisBtn.setPrefWidth(120);
      this.freqAnalysisBtn.setOnMousePressed(new FreqAnalysisBtnListener());
      this.mppAnalysisBtn.setOnMousePressed(new MPPAnalysisBtnListener());
      this.icAnalysisBtn.setOnMousePressed(new ICAnalysisBtnListener());

      HBox containerBox = new HBox();
      containerBox.setSpacing(10);
      containerBox.getChildren()
          .addAll(getTitle("Language"), languageComboBox, freqAnalysisBtn, mppAnalysisBtn, icAnalysisBtn);

      this.setSpacing(10);
      this.getChildren().addAll(langGraphPane, containerBox, ciphertextPane);
    }

    private Label getTitle(String title) {
      Label label = new Label(title);
      label.setFont(javafx.scene.text.Font.font("ARIAL", FontWeight.BOLD, 13));
      return label;
    }

    private void createMPPStage(String language, String langGraph, String ciphertextEncoding, String ciphertext) {
      MPPStage mppStage = new MPPStage("Most Probable Pair", this.getWidth(), this.getHeight());
      mppStage.createTable(language, langGraph, ciphertextEncoding, ciphertext);
      mppStage.show();
    }

    private void createFreqAnalysisStage(String language, String langGraph, String ciphertextEncoding,
        String ciphertext) {
      FreqAnalysisStage freqAnalysisStage = new FreqAnalysisStage("Frequency analysis", this.getWidth(),
          this.getHeight());
      freqAnalysisStage.createChart(language, langGraph, ciphertextEncoding, ciphertext);
      freqAnalysisStage.show();
    }

    private void createIndexCoincidenceStage(String language, String langGraph, String ciphertextEncoding,
        String ciphertext) {
      IndexCoincidenceStage icStage = new IndexCoincidenceStage("Index of coincidence", this.getWidth(),
          this.getHeight());
      icStage.createChart(language, ciphertextEncoding, ciphertext);
      icStage.show();
    }

    class ICAnalysisBtnListener implements EventHandler {

      @Override
      public void handle(Event event) {
        createIndexCoincidenceStage(languageComboBox.getValue(), langGraphPane.getSelectedToggle().getText(),
            null, ciphertextArea.getText());
      }
    }

    class MPPAnalysisBtnListener implements EventHandler {

      @Override
      public void handle(Event event) {
        createMPPStage(languageComboBox.getValue(), langGraphPane.getSelectedToggle().getText(),
            null, ciphertextArea.getText());
      }
    }

    @RequiredArgsConstructor
    class FreqAnalysisBtnListener implements EventHandler {

      @Override
      public void handle(Event event) {
        createFreqAnalysisStage(languageComboBox.getValue(), langGraphPane.getSelectedToggle().getText(),
           null, ciphertextArea.getText());
      }
    }

  }

  class BodyPane extends BorderPane {

    public BodyPane() {
    }
  }

}
