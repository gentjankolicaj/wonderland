package io.wonderland.rh.analysis;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.tree.BiTreeItem;
import io.wonderland.rh.cipher.CipherPane;
import io.wonderland.rh.hash.HashPane;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

@Slf4j
public class AnalysisTab extends Tab {
  private final Stage stage;
  private final BorderPane analysisWrapperPane = new BorderPane();

  public AnalysisTab(Stage stage, String title) {
    this.stage = stage;
    this.setText(title);
    this.setClosable(false);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createAnalysisPane());

    this.analysisWrapperPane.getChildren().add(getWelcomePane());

    splitPane.getItems().addAll(stackPane, analysisWrapperPane);
    splitPane.setDividerPositions(0.3f, 0.7f);

    this.setContent(splitPane);
  }


  private StackPane createAnalysisPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Create analysis panes
    Map<String, Class<? extends AnalysisPane>> children = AnalysisPane.empty().getChildren();
    for (Entry<String, Class<? extends AnalysisPane>> entry : children.entrySet()) {
      TreeItem<String> parentItem = getTreeItem(entry);
      rootItem.getChildren().add(parentItem);
    }

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectAnalysisPane(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }

  private TreeItem<String> getTreeItem(Entry<String, Class<? extends AnalysisPane>> entry) {
    TreeItem<String> parentItem = createTreeItem(entry);
    Map<String, Class<? extends AnalysisPane>> children = AnalysisPane.getClassChildren(entry.getValue());
    if (!MapUtils.isEmpty(children)) {
      for (Entry<String, Class<? extends AnalysisPane>> child : children.entrySet()) {
        TreeItem<String> childItem = getTreeItem(child);
        parentItem.getChildren().add(childItem);
      }
    }
    return parentItem;
  }

  private BiTreeItem<String> createTreeItem(Entry<String, Class<? extends AnalysisPane>> entry) {
    return new BiTreeItem<>(entry.getKey(), entry.getValue(), l -> {
      try {
        new AnalysisPane<>(new HBox(), GlobalConstants.WINDOW_WIDTH, GlobalConstants.WINDOW_HEIGHT);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    });
  }

  private void selectAnalysisPane(TreeItem<String> node) {
    try {
      if (!node.isLeaf()) {
        throw new IllegalArgumentException("Analysis not valid,please select an analysis option.");
      }
      BiTreeItem<String> biTreeItem = (BiTreeItem<String>) node;
      this.analysisWrapperPane.setCenter(
          AnalysisPane.newInstance(biTreeItem.getValue(), biTreeItem.getClazz()).getContainer());
    } catch (Exception e) {
      log.error(e.getMessage());
      this.analysisWrapperPane.setCenter(new BorderPane(new Label(e.getMessage())));
    }
  }

  private BorderPane getWelcomePane() {
    return new BorderPane(new Label("Welcome to analysis menu.Please select an analysis option from left..."));
  }


}

