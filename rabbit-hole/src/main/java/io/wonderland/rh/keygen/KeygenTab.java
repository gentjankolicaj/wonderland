package io.wonderland.rh.keygen;

import io.wonderland.rh.common.ServiceTab;
import io.wonderland.rh.exception.ServiceException;
import java.io.File;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeygenTab extends ServiceTab<KeyGenerator> {

  private static final String DEFAULT_SECRET_KEY_FILE_NAME = "secret_key";
  private static final String DEFAULT_PRIVATE_KEY_FILENAME = "private_key";
  public static final String DEFAULT_PUBLIC_KEY_FILENAME = "public_key";
  public static final String KEYGEN_ALGORITHM = "Keygen algorithm: ";
  private final HBox infoBox = new HBox();
  private BorderPane keyPane;
  private Optional<Object> optionalGen=Optional.empty();

  public KeygenTab(Stage stage, String title, String... serviceTypes) {
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

  protected KeyGenerator getKeyGenerator(String cspName, String serviceName) throws ServiceException {
    try {
      KeyGenerator tmp = KeyGenerator.getInstance(serviceName, cspName);
      log.info("Selected KeyGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyGenerator service '{}'", serviceName);
    }
    return null;
  }


  protected KeyPairGenerator getKeyPairGenerator(String cspName, String serviceName) throws ServiceException {
    try {
      KeyPairGenerator tmp = KeyPairGenerator.getInstance(serviceName, cspName);
      log.info("Selected KeyPairGenerator '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate KeyPairGenerator service '{}'", serviceName);
    }
    return null;
  }

  private Optional<Object> getSelectedKeygen(String cspName, String serviceType, String serviceName) {
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

      //Populate CSP node with correct cipher algorithm name
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
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectService(newItem));

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

  private SplitPane createKeygenIOPane() {
    SplitPane rootSplitter = new SplitPane();
    rootSplitter.setOrientation(Orientation.VERTICAL);

    this.keyPane = new BorderPane();
    this.keyPane.setTop(getMiscBox());

    rootSplitter.getItems().add(this.keyPane);
    rootSplitter.setDividerPositions(0.70f, 0.30f);
    rootSplitter.setPadding(new Insets(10, 10, 10, 10));
    return rootSplitter;
  }

  private VBox getMiscBox() {
    final VBox miscBox = new VBox();
    miscBox.setPadding(new Insets(5, 5, 5, 5));
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Keygen algorithm: ?"));

    //button
    HBox buttonBox = getButtonBox();

    miscBox.getChildren().addAll(this.infoBox, buttonBox);
    return miscBox;
  }


  private HBox getButtonBox() {
    HBox hBox = new HBox();

    Button keygenBtn = new Button("keygen");
    keygenBtn.setPrefWidth(120);
    keygenBtn.setOnMousePressed(new KeygenBtnReleased());

    Button exportBtn = new Button("export");
    exportBtn.setPrefWidth(120);
    exportBtn.setOnMousePressed(new ExportBtnReleased());

    hBox.setSpacing(10);
    hBox.getChildren().addAll(keygenBtn, exportBtn);
    return hBox;
  }


  private void selectService(TreeItem<String> node) {
    if (node.isLeaf()) {
      this.optionalGen = getSelectedKeygen(node.getParent().getValue(), node.getParent().getParent().getValue(), node.getValue());
      this.updateServiceView();
    }
  }

  private void updateServiceView() {
    if (optionalGen.isPresent()) {
      Object service = optionalGen.get();
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      if (service instanceof KeyGenerator) {
        this.keyPane.setCenter(new KeyGeneratorPane());
        this.infoBox.getChildren().add(new Label(KEYGEN_ALGORITHM + getKeyGeneratorDetails((KeyGenerator) service)));
      } else if (service instanceof KeyPairGenerator) {
        this.keyPane.setCenter(new KeyPairGeneratorPane());
        this.infoBox.getChildren()
            .add(new Label(KEYGEN_ALGORITHM + getKeyPairGeneratorDetails((KeyPairGenerator) service)));
      }
    } else {
      this.infoBox.getChildren().add(new Label(KEYGEN_ALGORITHM));
    }
  }


  private String getKeyGeneratorDetails(KeyGenerator keyGenerator) {
    if (Objects.nonNull(keyGenerator)) {
      return keyGenerator.getAlgorithm()+"  , CSP: "+keyGenerator.getProvider().getName();
    }
    return StringUtils.EMPTY;
  }

  private String getKeyPairGeneratorDetails(KeyPairGenerator keyPairGenerator) {
    if (Objects.nonNull(keyPairGenerator)) {
      return keyPairGenerator.getAlgorithm()+"  , CSP: "+keyPairGenerator.getProvider().getName();
    }
    return StringUtils.EMPTY;
  }



 private class KeygenBtnReleased implements EventHandler<Event> {
    @Override
    public void handle(Event event) {
      try {
        performKeygen();
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }

    /**
    * Generate key or key pair
    */
   private void performKeygen(){
     if(optionalGen.isPresent()) {
       Object generator= optionalGen.get();
       if (generator instanceof KeyGenerator) {
         KeyGenerator keyGenerator = (KeyGenerator) generator;
         KeyGeneratorPane keyGeneratorPane = (KeyGeneratorPane) keyPane.getCenter();
         keyGeneratorPane.update(keyGenerator.generateKey());
       } else if (generator instanceof KeyPairGenerator) {
         KeyPairGenerator keyPairGenerator = (KeyPairGenerator) generator;
         KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) keyPane.getCenter();
         keyPairGeneratorPane.update(keyPairGenerator.generateKeyPair());
       } else {
         log.warn("No 'SecretKey' or 'KeyPair' generated.");
       }
     }
   }
  }

  private class ExportBtnReleased implements EventHandler<Event>{
    @Override
    public void handle(Event event){
      try{
         performExport();
      }catch (Exception e){
        log.error(e.getMessage());
      }
    }

    private void performExport() {
      if(optionalGen.isPresent()) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Export data");
        final File file = dirChooser.showDialog(stage);
          Object generator= optionalGen.get();
          if (generator instanceof KeyGenerator) {
          KeyGeneratorPane keyGeneratorPane = (KeyGeneratorPane) keyPane.getCenter();
          CompletableFuture.runAsync(() -> exportKey(keyGeneratorPane.getSecretKey(), file.getPath(),DEFAULT_SECRET_KEY_FILE_NAME));
        } else if (generator instanceof KeyPairGenerator) {
          KeyPairGeneratorPane keyPairGeneratorPane = (KeyPairGeneratorPane) keyPane.getCenter();
          CompletableFuture.runAsync(() -> {
            exportKey(keyPairGeneratorPane.getKeyPair().getPrivate(), file.getPath(),DEFAULT_PRIVATE_KEY_FILENAME);
            exportKey(keyPairGeneratorPane.getKeyPair().getPublic(), file.getPath(), DEFAULT_PUBLIC_KEY_FILENAME);
          });
        }
      }
    }

    private void exportKey(Key key, String filePath,String filename) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, filename))) {
        os.write(key.getEncoded());
        os.flush();
      } catch (Exception e) {
        //do nothing yet
      }
    }

  }


}
