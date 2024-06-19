package io.wonderland.rh.analysis;

import io.wonderland.garden.api.GardenClient;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.analysis.service.BackgroundServices.GraphemeService;
import io.wonderland.rh.analysis.service.BackgroundServices.LetterFreqService;
import io.wonderland.rh.base.fx.BasePane;
import io.wonderland.rh.base.fx.HToggleBox;
import io.wonderland.rh.exception.BuildException;
import io.wonderland.rh.utils.GuiUtils;
import io.wonderland.struct.GramType;
import io.wonderland.struct.Language;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class LetterFrequencyPane extends BasePane<Void, StackPane, Void> {

  public static final String TITLE = "LETTER FREQUENCY WINDOW";
  private final BorderPane wrapperContainer = new BorderPane();
  private final Language language;
  private final GraphemeService graphemeService;
  private final LetterFreqService letterFreqService;
  private final HToggleBox<RadioButton> gramTypesPane = new HToggleBox<>("Grapheme: ", 10,
      RadioButton::new,
      Map.of("monogram", this::monogramChart, "digram", this::digramChart, "trigram",
          this::trigramChart));
  private final GardenClient gardenClient;

  public LetterFrequencyPane(String langCode, GardenClient gardenClient) {
    this.language = Language.ofNullable(langCode);
    this.gardenClient = gardenClient;
    this.graphemeService = new GraphemeService(language, container, wrapperContainer);
    this.letterFreqService = new LetterFreqService(container, wrapperContainer);

    //setup containers
    this.build();
  }

  private LetterFrequencyPane(double width, double height, String langCode,
      GardenClient gardenClient) {
    this.language = Language.ofNullable(langCode);
    this.gardenClient = gardenClient;
    this.graphemeService = new GraphemeService(language, container, wrapperContainer);
    this.letterFreqService = new LetterFreqService(container, wrapperContainer);
    this.sceneBuild(width, height);
  }

  @Override
  public StackPane createContainer() {
    return new StackPane();
  }

  @Override
  public void preBuild(Void unused) throws BuildException {
    graphemeService.downloadProcess();
  }

  public void build() {
    //setup containers
    this.container.getChildren().add(wrapperContainer);
    this.wrapperContainer.setTop(getControlBox());
    BorderPane.setMargin(wrapperContainer, GlobalConstants.DEFAULT_INSETS);
  }

  @Override
  public void sceneBuild(double sceneWidth, double sceneHeight, Void... args) {
    this.build();
    Stage stage = new Stage();
    Scene scene = new Scene(container, sceneWidth, sceneHeight);
    stage.setScene(scene);
    stage.setTitle(TITLE);
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
  public LetterFrequencyPane newInstance(Double width, Double height) {
    return new LetterFrequencyPane(width, height, language.getCode(), gardenClient);
  }

  private VBox getControlBox() {
    VBox controlBox = new VBox();
    controlBox.setSpacing(GlobalConstants.SPACING);
    controlBox.getChildren()
        .addAll(GuiUtils.getTitle(language + " letter frequency"), gramTypesPane);
    return controlBox;
  }

  private void monogramChart() {
    letterFreqService.chartProcess(language, GramType.MONOGRAM);
  }

  private void digramChart() {
    letterFreqService.chartProcess(language, GramType.DIGRAM);
  }

  private void trigramChart() {
    letterFreqService.chartProcess(language, GramType.TRIGRAM);
  }

}
