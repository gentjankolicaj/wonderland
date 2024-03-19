package io.wonderland.rh.mac;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.common.CustomTreeItem;
import io.wonderland.rh.base.common.TreeCellImpl;
import io.wonderland.rh.base.common.ServiceTab;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
public class MacTab extends ServiceTab {
  private final BorderPane macPaneWrapper =new BorderPane();

  public MacTab(Stage stage,String title, String serviceType) {
    super(stage, title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createMacPane());

    this.macPaneWrapper.setCenter(getWelcomePane());

    splitPane.getItems().addAll(stackPane, macPaneWrapper);
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

  private StackPane createMacPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //Cryptographic Service Provider nodes
    List<TreeItem<String>> cspNodes = getCSPNodes();

    //Populate CSP node with correct cipher algorithm name
    for (TreeItem<String> cspNode : cspNodes) {
      List<CustomTreeItem<String>> nameNodes= getNameNodes(cspNode.getValue());
      if(CollectionUtils.isNotEmpty(nameNodes)) {
        cspNode.getChildren().addAll(nameNodes);
        //Add CSP nodes to parent
        rootItem.getChildren().add(cspNode);
      }
    }

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.setCellFactory(f->new TreeCellImpl());
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

  private List<CustomTreeItem<String>> getNameNodes(String cspName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream().filter(s -> Arrays.stream(serviceTypes).anyMatch(st->st.equals(s.getType())))
        .map(Service::getAlgorithm).filter(this::isValidServiceName).sorted(Comparator.comparing(s -> s.charAt(0)))
        .map(this::getCustomTreeItem).collect(Collectors.toList());
  }

  private void selectMessageDigest(TreeItem<String> node) {
    this.updateContentPane(node);
  }

  private void updateContentPane(TreeItem<String> node) {
    try {
      if(!node.isLeaf()){
        throw new IllegalArgumentException("MAC function not valid,please select a MAC.");
      }
      this.macPaneWrapper.setCenter(new MacPane(stage,node.getValue()));
    } catch (Exception e) {
      log.error(e.getMessage());
      this.macPaneWrapper.setCenter(new Label(e.getMessage()));
    }
  }

  private Pane getWelcomePane(){
    BorderPane pane=new BorderPane();
    Label welcomeLbl=new Label("Welcome to MAC menu.Please a MAC function from left...");
    pane.setCenter(welcomeLbl);
    return pane;
  }

  private CustomTreeItem<String> getCustomTreeItem(String element) {
    return new CustomTreeItem<>(element, arg -> {
      try {
        new MacPane(element, GlobalConstants.WINDOW_WIDTH, GlobalConstants.WINDOW_HEIGHT);
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    });
  }

}
