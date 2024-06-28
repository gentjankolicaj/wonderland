package io.wonderland.rh.cipher;


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
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;


@Slf4j
public class CipherTab extends BaseTab {

  private final BorderPane container = new BorderPane();

  public CipherTab(String title, String serviceType) {
    super(title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createCiphersPane());

    this.container.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, container);
    splitPane.setDividerPositions(0.2f, 0.8f);

    this.setContent(splitPane);
  }

  private Node createCiphersPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Cryptographic Service Provider nodes
    List<CSPTreeItem> cspNodes = getCSPNodes();

    //Populate CSP node with correct cipher algorithm name
    for (CSPTreeItem cspNode : cspNodes) {
      List<MonoTreeItem<String>> cipherNameNodes = getCipherNodes(cspNode.getName());
      if (CollectionUtils.isNotEmpty(cipherNameNodes)) {
        cspNode.getChildren().addAll(cipherNameNodes);

        //Add CSP nodes to parent
        rootItem.getChildren().add(cspNode);
      }
    }

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.setCellFactory(f -> new TreeViewCellImpl());
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectCipher(newItem));

    return treeView;
  }


  private List<CSPTreeItem> getCSPNodes() {
    return Arrays.stream(Security.getProviders())
        .map(csp -> new CSPTreeItem(csp.getName(), csp.getVersionStr()))
        .sorted(Comparator.comparing(e -> e.getValue().charAt(0))).collect(Collectors.toList());
  }

  private List<MonoTreeItem<String>> getCipherNodes(String cspName) {
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

  private void selectCipher(TreeItem<String> node) {
    this.updateCipherPane(node);
  }

  private void updateCipherPane(TreeItem<String> node) {
    //Update cipher pane
    try {
      if (!node.isLeaf()) {
        throw new IllegalArgumentException("Cipher not valid,please select a cipher.");
      }
      String cspName = parseCspName(node.getParent().getValue());
      this.container.setCenter(new CipherPane(cspName, node.getValue()));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.container.setCenter(new BorderPane(new Label(e.getMessage())));
    }
  }

  private BorderPane getWelcomePane() {
    return new BorderPane(new Label("Welcome to cipher menu.Please select a cipher from left..."));
  }

  private MonoTreeItem<String> getCustomTreeItem(String cspName, String cipherName) {
    return new MonoTreeItem<>(cipherName, arg -> {
      try {
        new CipherPane(cspName, cipherName, GlobalConstants.SCENE_WIDTH,
            GlobalConstants.SCENE_HEIGHT);
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    });
  }

}
