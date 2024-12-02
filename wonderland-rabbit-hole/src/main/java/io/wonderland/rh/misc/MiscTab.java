package io.wonderland.rh.misc;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.BiTreeItem;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.NodeItem;
import io.wonderland.rh.base.fx.TreeViewCellImpl;
import io.wonderland.rh.base.fx.base.BasePane;
import io.wonderland.rh.base.fx.base.BaseTab;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MiscTab extends BaseTab {

  private static final List<NodeItem<BasePane<?, ?, ?>>> MISC_ITEMS = getMiscItems();
  private final BorderPane container = new BorderPane();

  public MiscTab(String title) {
    super(title);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createMiscPane());

    this.container.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, container);
    splitPane.setDividerPositions(0.3f, 0.7f);

    this.setContent(splitPane);
  }

  private static List<NodeItem<BasePane<?, ?, ?>>> getMiscItems() {
    return List.of(new NodeItem<>("Encoding", new EncodingPane()),
        new NodeItem<>("Barcode", new BarcodePane()));
  }

  private Node createMiscPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    List<BiTreeItem<String, NodeItem<?>>> miscItems = createTreeItems();
    rootItem.getChildren().addAll(miscItems);

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.setCellFactory(f -> new TreeViewCellImpl());
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectMisc(newItem));

    return treeView;
  }

  private void selectMisc(TreeItem<String> node) {
    if (node.isLeaf()) {
      Optional<NodeItem<BasePane<?, ?, ?>>> optionalMisc = getSelectedMisc(node.getValue());
      optionalMisc.ifPresent(this::updateContentPane);
    }
  }

  private Optional<NodeItem<BasePane<?, ?, ?>>> getSelectedMisc(String miscKey) {
    if (StringUtils.isEmpty(miscKey)) {
      return Optional.empty();
    }
    for (NodeItem<BasePane<?, ?, ?>> item : MISC_ITEMS) {
      if (item.getKey().equals(miscKey)) {
        return Optional.of(item);
      }
    }
    return Optional.empty();
  }

  private void updateContentPane(NodeItem<BasePane<?, ?, ?>> nodeItem) {
    try {
      this.container.setCenter(nodeItem.getNode().getContainer());
    } catch (Exception e) {
      log.error(e.getMessage());
      this.container.setCenter(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane() {
    BorderPane pane = new BorderPane();
    Label welcomeLbl = new Label("Welcome to misc.Please a misc from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }

  private List<BiTreeItem<String, NodeItem<?>>> createTreeItems() {
    List<BiTreeItem<String, NodeItem<?>>> items = new ArrayList<>();
    for (NodeItem<?> item : MISC_ITEMS) {
      items.add(toTreeItem(item));
    }
    return items;
  }

  private BiTreeItem<String, NodeItem<?>> toTreeItem(NodeItem<?> nodeItem) {
    return new BiTreeItem<>(nodeItem.getKey(), nodeItem, l -> {
      try {
        //create new window from item pane
        Method method = nodeItem.getNode().getClass()
            .getMethod("newInstance", Double.class, Double.class);
        method.invoke(nodeItem.getNode(), GlobalConstants.SCENE_WIDTH,
            GlobalConstants.SCENE_HEIGHT);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    });
  }

}

