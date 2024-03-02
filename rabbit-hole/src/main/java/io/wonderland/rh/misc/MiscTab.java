package io.wonderland.rh.misc;

import io.wonderland.rh.common.AbstractTab;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class MiscTab extends AbstractTab {

  private final HBox infoBox = new HBox();
  private BorderPane miscPane=new BorderPane();
  private Optional<Misc> optionalMisc=Optional.empty();

  public MiscTab(Stage stage,String title) {
    super(stage, title);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createMiscPane());

    final BorderPane borderPane = new BorderPane();
    borderPane.setCenter(createMiscIOPane());

    splitPane.getItems().addAll(stackPane, borderPane);
    splitPane.setDividerPositions(0.3f, 0.7f);

    this.setContent(splitPane);
  }


  private StackPane createMiscPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    TreeItem<String> encodingItem=new TreeItem<>(Misc.ENCODING.getLabel());
    rootItem.getChildren().add(encodingItem);

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectService(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }


  private void selectService(TreeItem<String> node) {
    if(node.isLeaf()) {
      this.optionalMisc = getSelectedMisc(node.getValue());
      this.updateServiceView();
    }
  }

  private Optional<Misc> getSelectedMisc(String miscLbl){
    if(StringUtils.isEmpty(miscLbl)){
      return Optional.empty();
    }

    for(Misc misc : Misc.values()){
      if(misc.getLabel().equals(miscLbl)){
        return Optional.of(misc);
      }
    }
    return Optional.empty();
  }

  private void updateServiceView() {
    if(optionalMisc.isPresent()){
    if (!infoBox.getChildren().isEmpty()) {
      this.infoBox.getChildren().remove(0);
    }
     Misc misc=optionalMisc.get();
      if(misc.equals(Misc.ENCODING)){
        this.miscPane.setCenter(new EncodingPane());
        this.infoBox.getChildren().add(new Label(" "));
      }
    } else {
      this.infoBox.getChildren().add(new Label("Please choose on misc option on the left."));
    }
  }


  private SplitPane createMiscIOPane() {
    SplitPane rootSplitter = new SplitPane();
    rootSplitter.setOrientation(Orientation.VERTICAL);

    BorderPane mainPane = new BorderPane();
    mainPane.setPadding(new Insets(10,10,10,10));

    mainPane.setTop(getMiscBox());

    rootSplitter.getItems().add(mainPane);
    rootSplitter.setDividerPositions(0.70f, 0.30f);
    return rootSplitter;
  }

  private VBox getMiscBox() {
    final VBox miscBox = new VBox();
    miscBox.setPadding(new Insets(5,5,5,5));
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Please choose on misc option on the left."));

    miscBox.getChildren().addAll(this.infoBox);
    return miscBox;
  }










}
