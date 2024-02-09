package io.wonderland.rh.cryptanalysis;

import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.common.TextPane;
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

@Slf4j
public class CiphertextAnalysisPane extends BorderPane {

  public static final String CIPHERTEXT = "Ciphertext";
  private final BodyPane bodyPane = new BodyPane();
  private final HeaderPane headerPane = new HeaderPane(bodyPane);


  public CiphertextAnalysisPane() {
    init();
  }

  private void init() {
    this.setTop(this.headerPane);
    this.setCenter(this.bodyPane);
  }


  class HeaderPane extends VBox {

    private final TextArea ciphertextArea = new TextArea();
    private final BodyPane bodyPane;
    private final ComboBox<String> languageComboBox = new ComboBox<>(FXCollections.observableArrayList(
        Arrays.stream(Language.values()).map(e -> e.getName()).collect(Collectors.toList())));
    private final Button freqAnalysisBtn = new Button("Freq analysis");
    private final Button mppAnalysisBtn = new Button("MPP analysis");
    private final Button icAnalysisBtn = new Button("IC analysis");
    private final HTogglePane<RadioButton> ciphertextEncodingPane = new HTogglePane<>("Ciphertext encoding : ",
        s->new RadioButton(s),Map.of("byte",()->{},
        "char", ()->{},"int",()->{}));
    private final HTogglePane<RadioButton> langGraphPane = new HTogglePane<>("Character graph : ", s->new RadioButton(s),
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
      this.getChildren().addAll(langGraphPane, ciphertextEncodingPane, containerBox, ciphertextPane);
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
            ciphertextEncodingPane.getSelectedToggle().getText(), ciphertextArea.getText());
      }
    }

    class MPPAnalysisBtnListener implements EventHandler {

      @Override
      public void handle(Event event) {
        createMPPStage(languageComboBox.getValue(), langGraphPane.getSelectedToggle().getText(),
            ciphertextEncodingPane.getSelectedToggle().getText(), ciphertextArea.getText());
      }
    }

    @RequiredArgsConstructor
    class FreqAnalysisBtnListener implements EventHandler {

      @Override
      public void handle(Event event) {
        createFreqAnalysisStage(languageComboBox.getValue(), langGraphPane.getSelectedToggle().getText(),
            ciphertextEncodingPane.getSelectedToggle().getText(), ciphertextArea.getText());
      }
    }

  }

  class BodyPane extends BorderPane {

    public BodyPane() {
    }
  }

}
