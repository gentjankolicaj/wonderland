package io.wonderland.rh.analysis.ciphertext;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.analysis.service.BackgroundServices;
import io.wonderland.rh.analysis.service.BackgroundServices.IOCService;
import io.wonderland.rh.base.fx.AbstractPane;
import io.wonderland.rh.base.fx.TextPane;
import io.wonderland.rh.utils.GuiUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Slf4j
public class IndexCoincidencePane extends AbstractPane<Void, StackPane> {

  public static final String KEY_MIN_LENGTH = "1";
  public static final String KEY_MAX_LENGTH = "5";
  public static final String PANE_TITLE = "Index of Coincidence";
  public static final String MIN_LENGTH = "Key min length: ";
  public static final String MAX_LENGTH = "Key max length:";
  private final ScrollPane scrollPane = new ScrollPane();
  private final BorderPane wrapperContainer = new BorderPane();

  private final Label title = GuiUtils.getTitle(PANE_TITLE);
  private final Button processBtn = new Button("process");
  private final TextField keyMinLength = new TextField(KEY_MIN_LENGTH);
  private final TextField keyMaxLength = new TextField(KEY_MAX_LENGTH);
  private final TextArea ciphertextArea = new TextArea();
  private final BackgroundServices.IOCService iocService = new IOCService(container,
      wrapperContainer);
  private final VBox controlBox = createControlBox();

  public IndexCoincidencePane() {
    //setup containers
    this.build();
  }

  private IndexCoincidencePane(double width, double height) {
    buildScene(width, height);
  }


  @Override
  public StackPane createContainer() {
    return new StackPane();
  }

  private void build() {
    //setup containers
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
  public IndexCoincidencePane newInstance(Double width, Double height) {
    return new IndexCoincidencePane(width, height);
  }


  private VBox createControlBox() {
    this.processBtn.setOnAction(
        actionEvent -> iocService.chartProcess(getKeyMinLength().getText(),
            getKeyMaxLength().getText(),
            getCiphertextArea()));

    //build container box
    VBox containerBox = new VBox();
    containerBox.setSpacing(GlobalConstants.SPACING);
    containerBox.getChildren().addAll(title, createKeyLengthBox(MIN_LENGTH, keyMinLength),
        createKeyLengthBox(MAX_LENGTH, keyMaxLength), new TextPane("Ciphertext", ciphertextArea),
        processBtn);
    return containerBox;
  }

  private HBox createKeyLengthBox(String label, TextField textField) {
    HBox hBox = new HBox();
    hBox.setSpacing(GlobalConstants.SPACING);
    hBox.getChildren().addAll(GuiUtils.getTitle(label), textField);
    return hBox;
  }


}
