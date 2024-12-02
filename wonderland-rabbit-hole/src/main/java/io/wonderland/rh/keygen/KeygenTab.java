package io.wonderland.rh.keygen;

import io.atlassian.fugue.Either;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.AbstractTreeItem;
import io.wonderland.rh.base.fx.CSPTreeItem;
import io.wonderland.rh.base.fx.MonoTreeItem;
import io.wonderland.rh.base.fx.TreeViewCellImpl;
import io.wonderland.rh.base.fx.base.BaseTab;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeygenTab extends BaseTab {

  private final BorderPane container = new BorderPane();

  public KeygenTab(String title, String... serviceTypes) {
    super(title, serviceTypes);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createKeygenPane());

    this.container.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, container);
    splitPane.setDividerPositions(0.2f, 0.8f);

    this.setContent(splitPane);
  }

  protected static KeyGenerator getKeyGenerator(String cspName, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    KeyGenerator tmp = KeyGenerator.getInstance(serviceName, cspName);
    log.info("Selected KeyGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
        tmp.getProvider().getName());
    return tmp;
  }

  protected static KeyPairGenerator getKeyPairGenerator(String cspName, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    KeyPairGenerator tmp = KeyPairGenerator.getInstance(serviceName, cspName);
    log.info("Selected KeyPairGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
        tmp.getProvider().getName());
    return tmp;
  }

  private static Either<KeyGenerator, KeyPairGenerator> getSelectedKeygen(String cspName,
      String serviceName) {
    if (StringUtils.isEmpty(serviceName)) {
      throw new IllegalArgumentException("KeyGenerator/KeyPairGenerator empty.");
    }
    try {
      return Either.left(getKeyGenerator(cspName, serviceName));
    } catch (Exception e) {
      log.error("{}", e.getMessage());
    }
    try {
      return Either.right(getKeyPairGenerator(cspName, serviceName));
    } catch (Exception e) {
      log.error("{}", e.getMessage());
    }
    throw new IllegalStateException("Failed to instantiate KeyGenerator/KeyPairGenerator");
  }

  private Node createKeygenPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Service nodes (KeyGenerator & KeyPairGenerator)
    List<TreeItem<String>> serviceNodes = getServiceNodes();

    for (TreeItem<String> serviceNode : serviceNodes) {

      //Cryptographic Service Provider (CSP) nodes
      List<CSPTreeItem> cspNodes = getCSPNodes();

      //Populate CSP node with correct keygen algorithm name
      for (CSPTreeItem cspNode : cspNodes) {
        List<AbstractTreeItem<String>> keygenNodes = getKeygenNodes(cspNode.getName(),
            serviceNode.getValue());
        if (CollectionUtils.isNotEmpty(keygenNodes)) {
          cspNode.getChildren().addAll(keygenNodes);
          serviceNode.getChildren().add(cspNode);
        }
      }
    }

    //Add all CSP nodes to parent
    rootItem.getChildren().addAll(serviceNodes);

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.setCellFactory(f -> new TreeViewCellImpl());
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectKeygen(newItem));

    return treeView;
  }

  private List<TreeItem<String>> getServiceNodes() {
    return Arrays.stream(serviceTypes).map(TreeItem::new).collect(Collectors.toList());
  }

  private List<CSPTreeItem> getCSPNodes() {
    return Arrays.stream(Security.getProviders())
        .map(csp -> new CSPTreeItem(csp.getName(), csp.getVersionStr()))
        .sorted(Comparator.comparing(e -> e.getValue().charAt(0))).collect(Collectors.toList());
  }

  private List<AbstractTreeItem<String>> getKeygenNodes(String cspName, String serviceName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream().filter(s -> serviceName.equals(s.getType()))
        .map(Service::getAlgorithm).filter(this::isValidServiceName)
        .sorted(Comparator.comparing(s -> s.charAt(0)))
        .map(e -> toTreeItem(cspName, e)).collect(Collectors.toList());
  }

  private void selectKeygen(TreeItem<String> node) {
    this.updateContentPane(node);
  }

  private void updateContentPane(TreeItem<String> node) {
    try {
      if (!node.isLeaf()) {
        throw new IllegalArgumentException("Please select a valid KeyGenerator/KeyPairGenerator.");
      }
      Either<KeyGenerator, KeyPairGenerator> keygen = getSelectedKeygen(
          ((CSPTreeItem) node.getParent()).getName(),
          node.getValue());
      this.container.setCenter(new KeygenPane(keygen));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.container.setCenter(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane() {
    BorderPane pane = new BorderPane();
    Label welcomeLbl = new Label(
        "Welcome to keygen menu.Please a KeyGenerator/KeyPairGenerator from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }

  private MonoTreeItem<String> toTreeItem(String cspName, String keygenName) {
    return new MonoTreeItem<>(keygenName, arg -> {
      try {
        Either<KeyGenerator, KeyPairGenerator> keygen = getSelectedKeygen(cspName, keygenName);
        new KeygenPane(keygen, GlobalConstants.SCENE_WIDTH, GlobalConstants.SCENE_HEIGHT);
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    });
  }

}
