package io.wonderland.rh.analysis;


import io.wonderland.garden.api.GardenClient;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.analysis.ciphertext.CiphertextFrequencyPane;
import io.wonderland.rh.analysis.ciphertext.IndexCoincidencePane;
import io.wonderland.rh.api.Clients;
import io.wonderland.rh.base.fx.AbstractPane;
import io.wonderland.rh.base.fx.AbstractTab;
import io.wonderland.rh.base.fx.BiTreeItem;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.NodeItem;
import io.wonderland.rh.base.fx.TreeViewCellImpl;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class AnalysisTab extends AbstractTab {

  private final BorderPane container = new BorderPane();
  private final StackPane treeContainer = new StackPane();
  private final NodeItem<AbstractPane<?, ?>> languageNode = new NodeItem<>("Language", "Language");
  private final NodeItem<AbstractPane<?, ?>> ciphertextNode = new NodeItem<>("Ciphertext",
      "Ciphertext");

  public AnalysisTab(String title) {
    super(title);
    SplitPane splitPane = new SplitPane();

    //stack pane
    this.container.setCenter(getWelcomePane());

    splitPane.getItems().addAll(treeContainer, container);
    splitPane.setDividerPositions(0.2f, 0.8f);

    //Run async task to init tree leafs
    initTreeItems();
    this.setContent(splitPane);
  }


  private void initTreeItems() {
    Platform.runLater(() -> {
      createCiphertextItems();
      createLangItems();
      rebuildTreeView();
    });
  }

  private void createCiphertextItems() {
    ciphertextNode.addChild(new NodeItem<>("Index of Coincidence", new IndexCoincidencePane()));
    Optional<GardenClient> optional = Clients.getGardenClient();
    if (optional.isPresent()) {
      GardenClient gardenClient = optional.get();
      ciphertextNode.addChild(
          new NodeItem<>("Ciphertext Frequency", new CiphertextFrequencyPane(gardenClient)));
    }
  }

  private void createLangItems() {
    if (Clients.getGardenClient().isPresent()) {
      GardenClient gardenClient = Clients.getGardenClient().get();
      try {
        List<String> langCodes = gardenClient.getLetterFreqLangCodes();
        List<NodeItem<AbstractPane<?, ?>>> children = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(langCodes)) {
          for (String langCode : langCodes) {
            children.add(new NodeItem<>(langCode, new LetterFrequencyPane(langCode, gardenClient)));
          }
        }

        this.languageNode.getChildren().addAll(children);
      } catch (Exception e) {
        log.error("{}", e.getMessage(), e);
      }
    }
  }

  private void rebuildTreeView() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Language item
    BiTreeItem<String, NodeItem<?>> langItem = getTreeItem(languageNode);

    //Ciphertext item
    BiTreeItem<String, NodeItem<?>> ciphertextItem = getTreeItem(ciphertextNode);

    //root item add children
    rootItem.getChildren().add(langItem);
    rootItem.getChildren().add(ciphertextItem);

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.setCellFactory(f -> new TreeViewCellImpl());
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectAnalysisPane(newItem));
    treeContainer.getChildren().removeAll(treeContainer.getChildren());
    treeContainer.getChildren().add(treeView);
  }

  private BiTreeItem<String, NodeItem<?>> getTreeItem(NodeItem<?> nodeItem) {
    BiTreeItem<String, NodeItem<?>> parentItem = toTreeItem(nodeItem);
    List<? extends NodeItem<?>> children = nodeItem.getChildren();
    if (!CollectionUtils.isEmpty(children)) {
      for (NodeItem<?> child : children) {
        BiTreeItem<String, NodeItem<?>> childItem = getTreeItem(child);
        parentItem.getChildren().add(childItem);
      }
    }
    return parentItem;
  }

  private BiTreeItem<String, NodeItem<?>> toTreeItem(NodeItem<?> nodeItem) {
    return new BiTreeItem<>(nodeItem.getKey(), nodeItem, l -> {
      try {
        //create new window from item pane
        Method method = nodeItem.getNode().getClass()
            .getMethod("newInstance", Double.class, Double.class);
        method.invoke(nodeItem.getNode(), GlobalConstants.WINDOW_WIDTH,
            GlobalConstants.WINDOW_HEIGHT);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    });
  }

  private void selectAnalysisPane(TreeItem<String> treeItem) {
    try {
      if (!treeItem.isLeaf()) {
        throw new IllegalArgumentException("Please select an valid analysis option.");
      }
      NodeItem<AbstractPane<?, ?>> nodeItem = ((BiTreeItem<String, NodeItem<AbstractPane<?, ?>>>) treeItem).getNode();
      AbstractPane<?, ?> pane = nodeItem.getNode();
      pane.preBuild(null);
      this.container.setCenter(pane.getContainer());
    } catch (Exception e) {
      this.container.setCenter(new BorderPane(new Label(e.getMessage())));
    }
  }

  private BorderPane getWelcomePane() {
    return new BorderPane(
        new Label("Welcome to analysis menu.Please select an analysis option from left..."));
  }

}

