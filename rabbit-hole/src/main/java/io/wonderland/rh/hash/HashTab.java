package io.wonderland.rh.hash;

import io.wonderland.rh.common.ServiceTab;
import io.wonderland.rh.exception.ServiceException;
import java.security.MessageDigest;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HashTab extends ServiceTab {
  private final BorderPane hashPaneWrapper=new BorderPane();
  private Optional<MessageDigest> messageDigest=Optional.empty();

  public HashTab(Stage stage,String title, String serviceType) {
    super(stage, title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createDigestsPane());

    this.hashPaneWrapper.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, hashPaneWrapper);
    splitPane.setDividerPositions(0.3f, 0.6f);

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

  private StackPane createDigestsPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Cryptographic Service Provider nodes
    List<TreeItem<String>> cspNodes = getCSPNodes();

    //Populate CSP node with correct cipher algorithm name
    for (TreeItem<String> cspNode : cspNodes) {
      List<TreeItem<String>> digestNameNodes=getDigestNameNodes(cspNode.getValue());
      if(CollectionUtils.isNotEmpty(digestNameNodes)) {
        cspNode.getChildren().addAll(digestNameNodes);
        //Add CSP nodes to parent
        rootItem.getChildren().addAll(cspNode);
      }
    }

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectMessageDigest(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }


  private List<TreeItem<String>> getCSPNodes() {
    List<String> cspNames = Arrays.stream(Security.getProviders()).map(Provider::getName).collect(Collectors.toList());
    return cspNames.stream().sorted(Comparator.comparing(s -> s.charAt(0))).map(TreeItem::new)
        .collect(Collectors.toList());
  }

  private List<TreeItem<String>> getDigestNameNodes(String cspName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream().filter(s -> Arrays.stream(serviceTypes).anyMatch(st->st.equals(s.getType())))
        .map(Service::getAlgorithm).filter(this::isValidServiceName).sorted(Comparator.comparing(s -> s.charAt(0))).map(
            TreeItem::new).collect(
            Collectors.toList());
  }

  private void selectMessageDigest(TreeItem<String> node) {
    this.updateContentPane(node);
  }

  private void updateContentPane(TreeItem<String> node) {
    try {
      if(!node.isLeaf()){
        throw new IllegalArgumentException("Hash function not valid,please select a hash.");
      }
      this.hashPaneWrapper.setCenter(new HashPane(stage,node.getValue()));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.hashPaneWrapper.setCenter(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane(){
    BorderPane pane=new BorderPane();
    Label welcomeLbl=new Label("Welcome to hash menu.Please a hash function from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }
}
