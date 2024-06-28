package io.wonderland.rh.hash;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.CSPTreeItem;
import io.wonderland.rh.base.fx.MonoTreeItem;
import io.wonderland.rh.base.fx.TreeViewCellImpl;
import io.wonderland.rh.base.fx.base.BaseTab;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class HashTab extends BaseTab {

  private final BorderPane container = new BorderPane();

  public HashTab(String title, String serviceType) {
    super(title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createDigestsPane());

    this.container.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, container);
    splitPane.setDividerPositions(0.2f, 0.8f);

    this.setContent(splitPane);
  }

  private Node createDigestsPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Cryptographic Service Provider nodes
    List<CSPTreeItem> cspNodes = getCSPNodes();

    //Populate CSP node with correct cipher algorithm name
    for (CSPTreeItem cspNode : cspNodes) {
      List<MonoTreeItem<String>> digestNameNodes = getDigestNameNodes(cspNode.getName());
      if (CollectionUtils.isNotEmpty(digestNameNodes)) {
        cspNode.getChildren().addAll(digestNameNodes);
        //Add CSP nodes to parent
        rootItem.getChildren().add(cspNode);
      }
    }

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.setCellFactory(f -> new TreeViewCellImpl());
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectMessageDigest(newItem));

    return treeView;
  }


  private List<CSPTreeItem> getCSPNodes() {
    return Arrays.stream(Security.getProviders())
        .map(csp -> new CSPTreeItem(csp.getName(), csp.getVersionStr()))
        .sorted(Comparator.comparing(e -> e.getValue().charAt(0))).collect(Collectors.toList());
  }

  private List<MonoTreeItem<String>> getDigestNameNodes(String cspName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream()
        .filter(s -> Arrays.stream(serviceTypes).anyMatch(st -> st.equals(s.getType())))
        .map(Service::getAlgorithm).filter(this::isValidServiceName)
        .sorted(Comparator.comparing(s -> s.charAt(0)))
        .map(e -> getCustomTreeItem(cspName, e)).collect(Collectors.toList());
  }

  private void selectMessageDigest(TreeItem<String> node) {
    this.updateContentPane(node);
  }

  private void updateContentPane(TreeItem<String> node) {
    try {
      if (!node.isLeaf()) {
        throw new IllegalArgumentException("Hash function not valid,please select a hash.");
      }
      String cspName = parseCspName(node.getParent().getValue());
      this.container.setCenter(new HashPane(cspName, node.getValue()));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.container.setCenter(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane() {
    BorderPane pane = new BorderPane();
    Label welcomeLbl = new Label("Welcome to hash menu.Please a hash function from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }

  private MonoTreeItem<String> getCustomTreeItem(String cspName, String digestName) {
    return new MonoTreeItem<>(digestName, arg -> {
      try {
        new HashPane(cspName, digestName, GlobalConstants.SCENE_WIDTH,
            GlobalConstants.SCENE_HEIGHT);
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    });
  }


}
