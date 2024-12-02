package io.wonderland.rh.analysis.ciphertext;


import io.wonderland.garden.api.GardenClient;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.analysis.service.BackgroundServices;
import io.wonderland.rh.analysis.service.BackgroundServices.CiphertextFreqService;
import io.wonderland.rh.base.fx.HToggleBox;
import io.wonderland.rh.base.fx.TextPane;
import io.wonderland.rh.base.fx.base.AbstractPane;
import io.wonderland.rh.utils.GuiUtils;
import io.wonderland.struct.GramType;
import io.wonderland.struct.Language;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;


@Getter
@Slf4j
public class CiphertextFrequencyPane extends AbstractPane<Void, StackPane> {

  private static final String PANE_TITLE = "Ciphertext frequency";
  private static final String COMBO_BOX_TEXT = "Choose language :";
  private final ScrollPane scrollPane = new ScrollPane();
  private final BorderPane wrapperContainer = new BorderPane();

  private final Label title = GuiUtils.getTitle(PANE_TITLE);
  private final SearchableComboBox<String> languageComboBox = new SearchableComboBox<>();
  private final TextArea ciphertextArea = new TextArea();
  private final Button processBtn = new Button("process");
  private final BackgroundServices.CiphertextFreqService ciphertextFreqService = new CiphertextFreqService(
      container,
      wrapperContainer);
  private final GardenClient gardenClient;
  private GramType gramType;
  private final HToggleBox<RadioButton> graphemePane = new HToggleBox<>("Grapheme: ", 10,
      RadioButton::new,
      Map.of("monogram", () -> setGramType(GramType.MONOGRAM), "digram",
          () -> setGramType(GramType.DIGRAM), "trigram",
          () -> setGramType(GramType.TRIGRAM)));
  private Language language;
  private final VBox controlBox = createControlBox();

  public CiphertextFrequencyPane(GardenClient gardenClient) {
    this.gardenClient = gardenClient;

    //setup container container
    this.build();
  }

  private CiphertextFrequencyPane(double width, double height, GardenClient gardenClient) {
    this.gardenClient = gardenClient;

    //setup container container
    this.buildScene(width, height);
  }

  @Override
  public StackPane createContainer() {
    return new StackPane();
  }

  private void build() {
    CompletableFuture.runAsync(this::updateLangCodes);

    //setup container container
    super.container.getChildren().add(scrollPane);

    this.scrollPane.setFitToWidth(true);
    this.scrollPane.setFitToHeight(true);
    this.scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
    this.scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    this.scrollPane.setContent(wrapperContainer);
    BorderPane.setMargin(wrapperContainer, GlobalConstants.DEFAULT_INSETS);
    this.wrapperContainer.setTop(controlBox);
  }

  private void buildScene(double width, double height) {
    this.build();
    //init scene & pane
    Scene scene = new Scene(container, width, height);
    Stage stage = new Stage();

    //init stage
    stage.setTitle(PANE_TITLE.toUpperCase() + " WINDOW");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Used by reflection to create new window
   *
   * @param width  of new window
   * @param height of new window
   * @return instance of this pane
   */
  @SuppressWarnings("unused")
  public CiphertextFrequencyPane newInstance(Double width, Double height) {
    return new CiphertextFrequencyPane(width, height, gardenClient);
  }

  private VBox createControlBox() {
    this.languageComboBox.setOnAction(new LangComboBoxEventHandler());
    this.languageComboBox.setPromptText(COMBO_BOX_TEXT);
    HBox.setHgrow(ciphertextArea, Priority.ALWAYS);

    this.processBtn.setOnAction(
        actionEvent -> ciphertextFreqService.chartProcess(getLanguage(), getGramType(),
            getCiphertextArea()));

    //build container box
    VBox containerBox = new VBox();
    containerBox.setSpacing(GlobalConstants.SPACING);
    containerBox.getChildren().addAll(title, new HBox(languageComboBox), graphemePane,
        new TextPane("Ciphertext", ciphertextArea), processBtn);
    return containerBox;
  }

  private void setGramType(GramType gramType) {
    this.gramType = gramType;
  }


  private void updateLangCodes() {
    if (gardenClient != null) {
      List<String> langCodes = gardenClient.getLetterFreqLangCodes();
      this.languageComboBox.setItems(FXCollections.observableArrayList(langCodes));
    }
  }


  private class LangComboBoxEventHandler implements javafx.event.EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      String value = languageComboBox.getValue();
      if (StringUtils.isNotEmpty(value)) {
        setLanguage(value);
      }
    }

    private void setLanguage(String comboValue) {
      language = Language.ofNullable(comboValue);
    }
  }


}
