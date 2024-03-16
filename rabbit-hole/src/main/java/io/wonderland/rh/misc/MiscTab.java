package io.wonderland.rh.misc;

import io.wonderland.rh.base.common.AbstractTab;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MiscTab extends AbstractTab {

  private final ScrollPane miscPaneWrapper =new ScrollPane();
  private Optional<Misc> optionalMisc=Optional.empty();

  public MiscTab(Stage stage,String title) {
    super(stage, title);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createMiscPane());

    this.miscPaneWrapper.setContent(getWelcomePane());

    splitPane.getItems().addAll(stackPane, miscPaneWrapper);
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
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectMisc(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }


  private void selectMisc(TreeItem<String> node) {
    if(node.isLeaf()) {
      this.optionalMisc = getSelectedMisc(node.getValue());
      this.updateContentPane();
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

  private void updateContentPane() {
    try {
      this.miscPaneWrapper.setContent( new MiscPane(optionalMisc));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.miscPaneWrapper.setContent(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane(){
    BorderPane pane=new BorderPane();
    Label welcomeLbl=new Label("Welcome to misc.Please a misc from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }


}
