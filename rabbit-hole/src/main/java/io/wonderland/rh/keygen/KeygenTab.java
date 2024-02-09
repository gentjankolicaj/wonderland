package io.wonderland.rh.keygen;

import io.wonderland.rh.common.ServiceTab;
import io.wonderland.rh.exception.ServiceException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeygenTab extends ServiceTab<KeyGenerator> {

  private final HBox infoBox = new HBox();

  private BorderPane keyPane;
  private Object generator;

  public KeygenTab(Stage stage,String title, String... serviceTypes) {
    super(stage, title, serviceTypes);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createKeygenPane());

    final BorderPane borderPane = new BorderPane();
    borderPane.setCenter(createKeygenIOPane());

    splitPane.getItems().addAll(stackPane, borderPane);
    splitPane.setDividerPositions(0.3f, 0.6f);

    this.setContent(splitPane);
  }

  @Override
  protected KeyGenerator getDefaultService() {
    return null;
  }

  @Override
  protected KeyGenerator getService(String serviceName) throws ServiceException {
    try {
      KeyGenerator tmp = KeyGenerator.getInstance(serviceName);
      log.info("Selected KeyGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyGenerator service '{}'", serviceName);
    }
    return null;
  }


  protected KeyGenerator getKeyGenerator(String cspName,String serviceName) throws ServiceException {
    try {
      KeyGenerator tmp = KeyGenerator.getInstance(serviceName,cspName);
      log.info("Selected KeyGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyGenerator service '{}'", serviceName);
    }
    return null;
  }


protected KeyPairGenerator getKeyPairGenerator(String cspName,String serviceName) throws ServiceException{
    try {
      KeyPairGenerator tmp = KeyPairGenerator.getInstance(serviceName,cspName);
      log.info("Selected KeyPairGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyPairGenerator service '{}'", serviceName);
    }
    return null;
  }

  private Object getService(String cspName,String serviceType,String serviceName){
    if(serviceType.equalsIgnoreCase("KeyPairGenerator"))
      return getKeyPairGenerator(cspName,serviceName);
    else if(serviceType.equalsIgnoreCase("KeyGenerator"))
      return getKeyGenerator(cspName,serviceName);
    else
      return null;
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
    List<TreeItem<String>> serviceNodes=getServiceNodes();

    for(TreeItem<String> serviceNode: serviceNodes) {

      //Cryptographic Service Provider (CSP) nodes
      List<TreeItem<String>> cspNodes = getCSPNodes();

      //Populate CSP node with correct cipher algorithm name
      for (TreeItem<String> cspNode : cspNodes) {
        List<TreeItem<String>> keygenNodes=getKeygenNodes(cspNode.getValue(),serviceNode.getValue());
        if(CollectionUtils.isNotEmpty(keygenNodes)) {
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
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectService(newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }


  private List<TreeItem<String>> getServiceNodes(){
    return Arrays.stream(serviceTypes).map(s->new TreeItem<>(s)).collect(Collectors.toList());
  }


  private List<TreeItem<String>> getCSPNodes() {
    List<String> cspNames = Arrays.stream(Security.getProviders()).map(Provider::getName).collect(Collectors.toList());
    return cspNames.stream().sorted(Comparator.comparing(s -> s.charAt(0))).map(TreeItem::new)
        .collect(Collectors.toList());
  }

  private List<TreeItem<String>> getKeygenNodes(String cspName,String serviceName) {
    Provider provider = Security.getProvider(cspName);
    if (provider == null) {
      return List.of();
    }
    return provider.getServices().stream().filter(s -> serviceName.equals(s.getType()))
        .map(Service::getAlgorithm).filter(this::isValidServiceName).sorted(Comparator.comparing(s -> s.charAt(0))).map(
            TreeItem::new).collect(
            Collectors.toList());
  }

  private SplitPane createKeygenIOPane() {
    SplitPane rootSplitter = new SplitPane();
    rootSplitter.setOrientation(Orientation.VERTICAL);

    this.keyPane = new BorderPane();
    this.keyPane.setTop(getMiscBox());

    rootSplitter.getItems().add(this.keyPane);
    rootSplitter.setDividerPositions(0.70f, 0.30f);
    return rootSplitter;
  }

  private VBox getMiscBox() {
    final VBox miscBox = new VBox();
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Name :    *** "));

    //button
    BorderPane buttonPane = getButtonPane();

    miscBox.getChildren().addAll(this.infoBox, buttonPane);
    return miscBox;
  }



  private BorderPane getButtonPane() {
    BorderPane pane = new BorderPane();

    //key scroll pane
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    //buttons
    VBox buttonBox = getButtonsBox();
    scrollPane.setContent(buttonBox);

    pane.setCenter(scrollPane);
    pane.setBorder(new Border(new BorderStroke(Color.BLACK,
        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    return pane;
  }

  private VBox getButtonsBox() {
    VBox box = new VBox();

    Button digestBtn = new Button("keygen");
    digestBtn.setPrefWidth(120);
    digestBtn.setOnMousePressed(new KeygenBtnReleased());

    box.setSpacing(10);
    box.getChildren().addAll(digestBtn);
    return box;
  }

  private void selectService(TreeItem<String> node) {
    if(node.isLeaf()) {
      this.generator = getService(node.getParent().getValue(), node.getParent().getParent().getValue(),node.getValue());
      this.updateServiceGui(generator);
    }
  }

  private void updateServiceGui(Object service) {
    if (service != null) {
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      if(service instanceof KeyGenerator){
        this.keyPane.setCenter(new KeyGeneratorPane());
        this.infoBox.getChildren().add(new Label("Name : "+getKeyGeneratorDetails((KeyGenerator) service)));
      }else if(service instanceof KeyPairGenerator){
       this. keyPane.setCenter(new KeyPairGeneratorPane());
        this.infoBox.getChildren().add(new Label("Name : "+getKeyPairGeneratorDetails((KeyPairGenerator) service)));
      }
    } else {
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      this.infoBox.getChildren().add(new Label("Name :"));
    }
  }


  private String getKeyGeneratorDetails(KeyGenerator keyGenerator) {
    if (Objects.nonNull(keyGenerator)) {
      return keyGenerator.getAlgorithm()+"  *** Provider : "+keyGenerator.getProvider().getName();
    }
    return StringUtils.EMPTY;
  }

  private String getKeyPairGeneratorDetails(KeyPairGenerator keyPairGenerator) {
    if (Objects.nonNull(keyPairGenerator)) {
      return keyPairGenerator.getAlgorithm()+"  *** Provider : "+keyPairGenerator.getProvider().getName();
    }
    return StringUtils.EMPTY;
  }

  /**
   * Generate key & update gui
   */
  private void performKeygen(){
    if(generator instanceof KeyGenerator){
      KeyGenerator keyGenerator=(KeyGenerator) generator;
      KeyGeneratorPane keyGeneratorPane=(KeyGeneratorPane) this.keyPane.getCenter();
      keyGeneratorPane.update(keyGenerator.generateKey());
    }else if( generator instanceof KeyPairGenerator){
      KeyPairGenerator keyPairGenerator=(KeyPairGenerator) generator;
      KeyPairGeneratorPane keyPairGeneratorPane=(KeyPairGeneratorPane) this.keyPane.getCenter();
      keyPairGeneratorPane.update(keyPairGenerator.generateKeyPair());
    }else{
      log.warn("No 'SecretKey' or 'KeyPair' generated.");
    }
  }

  class KeygenBtnReleased implements EventHandler<Event> {
    @Override
    public void handle(Event event) {
      try {
        performKeygen();
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

}
