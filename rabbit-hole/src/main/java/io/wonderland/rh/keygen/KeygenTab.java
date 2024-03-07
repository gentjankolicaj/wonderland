package io.wonderland.rh.keygen;

import io.wonderland.rh.common.ServiceTab;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeygenTab extends ServiceTab<KeyGenerator> {

 private final BorderPane keygenPaneWrapper =new BorderPane();

  public KeygenTab(Stage stage, String title, String... serviceTypes) {
    super(stage, title, serviceTypes);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createKeygenPane());

    this.keygenPaneWrapper.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, keygenPaneWrapper);
    splitPane.setDividerPositions(0.3f, 0.7f);

    this.setContent(splitPane);
  }


  @Override
  protected boolean isValidServiceName(String name) {
    if (StringUtils.isEmpty(name)) {
      return false;
    } else {
      return !(name.contains(".") || name.contains("OID"));
    }
  }

  private StackPane createKeygenPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Service nodes (KeyGenerator & KeyPairGenerator)
    List<TreeItem<String>> serviceNodes = getServiceNodes();

    for (TreeItem<String> serviceNode : serviceNodes) {

      //Cryptographic Service Provider (CSP) nodes
      List<TreeItem<String>> cspNodes = getCSPNodes();

      //Populate CSP node with correct keygen algorithm name
      for (TreeItem<String> cspNode : cspNodes) {
        List<TreeItem<String>> keygenNodes = getKeygenNodes(cspNode.getValue(), serviceNode.getValue());
        if (CollectionUtils.isNotEmpty(keygenNodes)) {
          cspNode.getChildren().addAll(keygenNodes);
          serviceNode.getChildren().add(cspNode);
        }
      }
    }

    //Add all CSP nodes to parent
    rootItem.getChildren().addAll(serviceNodes);

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectKeygen(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }


  private List<TreeItem<String>> getServiceNodes() {
    return Arrays.stream(serviceTypes).map(TreeItem::new).collect(Collectors.toList());
  }


  private List<TreeItem<String>> getCSPNodes() {
    List<String> cspNames = Arrays.stream(Security.getProviders()).map(Provider::getName).collect(Collectors.toList());
    return cspNames.stream().sorted(Comparator.comparing(s -> s.charAt(0))).map(TreeItem::new)
        .collect(Collectors.toList());
  }

  private List<TreeItem<String>> getKeygenNodes(String cspName, String serviceName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream().filter(s -> serviceName.equals(s.getType()))
        .map(Service::getAlgorithm).filter(this::isValidServiceName).sorted(Comparator.comparing(s -> s.charAt(0))).map(
            TreeItem::new).collect(
            Collectors.toList());
  }

  protected KeyGenerator getKeyGenerator(String cspName, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
      KeyGenerator tmp = KeyGenerator.getInstance(serviceName, cspName);
      log.info("Selected KeyGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
  }


  protected KeyPairGenerator getKeyPairGenerator(String cspName, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
      KeyPairGenerator tmp = KeyPairGenerator.getInstance(serviceName, cspName);
      log.info("Selected KeyPairGenerator '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
      return tmp;
  }

  private Optional<Object> getSelectedKeygen(String cspName, String serviceType, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    if (StringUtils.isEmpty(serviceType)) {
      return Optional.empty();
    }
    if (serviceType.equalsIgnoreCase("KeyPairGenerator"))
      return Optional.ofNullable(getKeyPairGenerator(cspName, serviceName));
    else if (serviceType.equalsIgnoreCase("KeyGenerator"))
      return Optional.ofNullable(getKeyGenerator(cspName, serviceName));
    else
      return Optional.empty();
  }
  private void selectKeygen(TreeItem<String> node) {
      this.updateContentPane(node);
  }


  private void updateContentPane(TreeItem<String> node) {
    try {
      if(!node.isLeaf()){
        throw new IllegalArgumentException("Key generator not valid,please select a key generator.");
      }
      Optional<Object> optionalKeygen = getSelectedKeygen(node.getParent().getValue(), node.getParent().getParent().getValue(), node.getValue());
      this.keygenPaneWrapper.setCenter(new KeygenPane(stage,optionalKeygen));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.keygenPaneWrapper.setCenter(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane(){
    BorderPane pane=new BorderPane();
    Label welcomeLbl=new Label("Welcome to keygen menu.Please a key generator from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }

}
