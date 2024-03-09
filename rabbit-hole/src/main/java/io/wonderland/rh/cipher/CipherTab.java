package io.wonderland.rh.cipher;


import io.wonderland.rh.common.ServiceTab;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class CipherTab extends ServiceTab<Cipher> {

  private final BorderPane cipherPaneWrapper =new BorderPane();

  public CipherTab(Stage stage, String title, String serviceType) {
    super(stage, title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createCiphersPane());

    this.cipherPaneWrapper.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, cipherPaneWrapper);
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

  private StackPane createCiphersPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Cryptographic Service Provider nodes
    List<TreeItem<String>> cspNodes = getCSPNodes();

    //Populate CSP node with correct cipher algorithm name
    for (TreeItem<String> cspNode : cspNodes) {
      List<TreeItem<String>> cipherNameNodes= getCipherNameNodes(cspNode.getValue());
      if(CollectionUtils.isNotEmpty(cipherNameNodes)) {
        cspNode.getChildren().addAll(cipherNameNodes);

        //Add CSP nodes to parent
        rootItem.getChildren().add(cspNode);
      }
    }

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectCipher(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }


  private List<TreeItem<String>> getCSPNodes() {
    List<String> cspNames = Arrays.stream(Security.getProviders()).map(Provider::getName).collect(Collectors.toList());
    return cspNames.stream().sorted(Comparator.comparing(s -> s.charAt(0))).map(TreeItem::new)
        .collect(Collectors.toList());
  }

  private List<TreeItem<String>> getCipherNameNodes(String cspName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream().filter(s -> Arrays.stream(serviceTypes).anyMatch(st->st.equals(s.getType())))
        .map(Service::getAlgorithm).filter(this::isValidServiceName).sorted(Comparator.comparing(s -> s.charAt(0))).map(
            TreeItem::new).collect(
            Collectors.toList());
  }


  private void selectCipher(TreeItem<String> node) {
    this.updateCipherPane(node);
  }



  private void updateCipherPane(TreeItem<String> node){
    //Update cipher pane
    try {
      if(!node.isLeaf()){
        throw new IllegalArgumentException("Cipher not valid,please select a cipher.");
      }
      this.cipherPaneWrapper.setCenter(new CipherPane(this.stage, node.getValue()));
    }catch (Exception e){
      log.error(e.getMessage());
      this.cipherPaneWrapper.setCenter(new BorderPane(new Label(e.getMessage())));
    }
  }

  private BorderPane getWelcomePane(){
   return new BorderPane(new Label("Welcome to cipher menu.Please select a cipher from left..."));
  }








}
