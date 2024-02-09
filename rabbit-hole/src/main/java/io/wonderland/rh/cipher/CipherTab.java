package io.wonderland.rh.cipher;


import io.wonderland.rh.cipher.key.DefaultKeyPane;
import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.common.ServiceTab;
import io.wonderland.rh.common.TextPane;
import io.wonderland.rh.exception.ServiceException;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class CipherTab extends ServiceTab<Cipher> {

  private final HBox infoBox = new HBox();
  private final TextArea plainTextArea = new TextArea();
  private final TextArea cipherTextArea = new TextArea();
  private Cipher encryptCipher = getDefaultService();
  private Cipher decryptCipher = getDefaultService();
  private boolean cipherStateStale = true;
  private KeyPane<?> keyPane;
  private BorderPane wrapperKeyPane;

  public CipherTab(Stage stage, String title, String serviceType) {
    super(stage, title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createCiphersPane());

    final BorderPane borderPane = new BorderPane();
    borderPane.setCenter(createCipherIOPane());

    splitPane.getItems().addAll(stackPane, borderPane);
    splitPane.setDividerPositions(0.3f, 0.6f);

    this.setContent(splitPane);
  }


  @Override
  protected Cipher getDefaultService() {
    Cipher tmp = null;
    try {
      tmp = Cipher.getInstance("Vigenere", "Alice");
      log.info("Default cipher '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    } catch (Exception e1) {
      log.warn("Error finding default  cipher 'Vigenere' - Alice.", e1);
      log.warn("Attempting to find random one.");

      try {
        List<String> cspNames = Arrays.stream(Security.getProviders()).map(Provider::getName)
            .collect(Collectors.toList());
        String transformationName = null;
        for (String cspName : cspNames) {
          Optional<String> optional = Security.getProvider(cspName).getServices().stream()
              .filter(s -> Arrays.stream(serviceTypes).anyMatch(st->st.equals(s.getType())))
              .map(Service::getAlgorithm).filter(this::isValidServiceName).findFirst();
          if (optional.isPresent()) {
            transformationName = optional.get();
            break;
          }
        }
        assert transformationName != null;
        tmp = Cipher.getInstance(transformationName);
        log.info("Default cipher '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
      } catch (Exception e2) {
        log.error("Failed to find random cipher to all CSP.", e2);
      }
    }
    updateServiceInfo(tmp);
    return tmp;
  }

  @Override
  protected Cipher getService(String serviceName) throws ServiceException {
    try {
      Cipher tmp = Cipher.getInstance(serviceName);
      log.info("Selected cipher '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate cipher service '{}'", serviceName);
    }
    return null;
  }

  private void staleCipherState() {
    this.cipherStateStale = true;
  }

  private void cipherInit() throws InvalidKeyException {
    if (this.cipherStateStale) {
      //init encrypt cipher
      try {
        this.encryptCipher.init(Cipher.ENCRYPT_MODE, keyPane.getCipherKey());
      } catch (Exception e) {
        log.error("Failed to init encrypt-cipher.", e);
        throw e;
      }
      //init decrypt cipher
      try {
        this.decryptCipher.init(Cipher.DECRYPT_MODE, keyPane.getCipherKey());
      } catch (Exception e) {
        log.error("Failed to init decrypt-cipher.", e);
        throw e;
      }

      //set a stale state to false, because ciphers are initialized with last keys
      this.cipherStateStale = false;
    }
  }

  protected void updateServiceInfo(Cipher cipher) {
    if (cipher != null) {
      if(!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
        this.infoBox.getChildren().add(new Label(
            "Name : " + cipher.getAlgorithm() + " , block-size : " + cipher.getBlockSize() + "  *** Provider : "
                + cipher.getProvider().getName()));
      }
    } else {
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      this.infoBox.getChildren().add(new Label("Name :"));
    }
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
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectCipher(newItem.getValue()));

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


  private void selectCipher(String cipherName) {
    if (StringUtils.isEmpty(cipherName)) {
      return;
    }
    this.encryptCipher = getService(cipherName);
    this.decryptCipher = getService(cipherName);
    this.staleCipherState();
    this.updateServiceInfo(encryptCipher);
    this.updateKeyPane(cipherName);
  }

  private void updateKeyPane(String cipher) {
    Class<?> keyPaneClass = CipherConstants.getKeyPaneMappings().computeIfAbsent(cipher, k -> DefaultKeyPane.class);
    try {
      Consumer<?> consumer = s -> staleCipherState();
      Constructor<?> constructor = keyPaneClass.getDeclaredConstructor(Consumer.class);
      KeyPane<?> newKeyPane = (KeyPane<?>) constructor.newInstance(consumer);

      //remove old key pane
      this.wrapperKeyPane.getChildren().remove(this.wrapperKeyPane.getCenter());

      //set new key pane
      this.keyPane = newKeyPane;
      this.wrapperKeyPane.setCenter(newKeyPane);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new ServiceException("Failed to changed KeyGeneratorPane", e);
    }
  }


  private SplitPane createCipherIOPane() {
    SplitPane rootSplitter = new SplitPane();
    rootSplitter.setOrientation(Orientation.VERTICAL);

    BorderPane mainPane = new BorderPane();
    mainPane.setTop(getMiscBox());
    mainPane.setCenter(getTextBox());

    rootSplitter.getItems().add(mainPane);
    rootSplitter.setDividerPositions(0.70f, 0.30f);
    return rootSplitter;
  }


  private HBox getTextBox() {
    //Message box for plain & cipher text
    HBox messageBox = new HBox();
    messageBox.getChildren()
        .addAll(new TextPane("Plaintext", plainTextArea), new TextPane("Ciphertext", cipherTextArea));
    return messageBox;
  }

  private VBox getMiscBox() {
    VBox miscBox = new VBox();
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Cipher name : " + getCipherName(encryptCipher) + "   ** "));

    //ciphertext encoding
    HBox encodingBox = getEncodingBox();

    //wrapper pane (cipher key + buttons)
    this.wrapperKeyPane = getWrapperKeyPane();

    miscBox.getChildren().addAll(this.infoBox, encodingBox, this.wrapperKeyPane);
    return miscBox;
  }

  private String getCipherName(Cipher cipher) {
    if (Objects.nonNull(cipher)) {
      int lastDot = cipher.getClass().getName().lastIndexOf(".");
      return cipher.getClass().getName().substring(lastDot + 1);
    }
    return StringUtils.EMPTY;
  }

  private BorderPane getWrapperKeyPane() {
    //Instantiate wrapper key pane
    BorderPane borderPane = new BorderPane();

    //create button box
    VBox buttonBox = getButtonsBox();

    //create default key pane
    this.keyPane = new DefaultKeyPane<>("default", s -> staleCipherState());

    //Assign children to wrapper pane
    borderPane.setCenter(keyPane);
    borderPane.setLeft(buttonBox);
    borderPane.setBorder(new Border(new BorderStroke(Color.BLACK,
        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    return borderPane;
  }

  private VBox getButtonsBox() {
    VBox btnBox = new VBox();

    Button encryptBtn = new Button("encrypt");
    Button decryptBtn = new Button("decrypt");
    Button clearBtn = new Button("clear");
    Button exportBtn = new Button("export");

    //set button width
    encryptBtn.setPrefWidth(120);
    decryptBtn.setPrefWidth(120);
    clearBtn.setPrefWidth(120);
    exportBtn.setPrefWidth(120);

    //set listeners
    encryptBtn.setOnMousePressed(new EncryptBtnReleased());
    decryptBtn.setOnMousePressed(new DecryptBtnReleased());
    clearBtn.setOnMousePressed(new ClearBtnReleased());
    exportBtn.setOnMousePressed(new ExportBtnPressed());

    btnBox.setSpacing(10);
    btnBox.getChildren().addAll(encryptBtn, decryptBtn, clearBtn, exportBtn);
    return btnBox;
  }

  private HBox getEncodingBox() {
    return new HTogglePane<>("Key encoding ", s->new RadioButton(s), Map.of("byte",()->{},
        "char", ()->{},"int",()->{}));
  }


  class EncryptBtnReleased implements EventHandler<Event> {


    @Override
    public void handle(Event event) {
      if (StringUtils.isEmpty(plainTextArea.getText())) {
        return;
      }
      try {
        //init cipher
        cipherInit();

        String plaintext = plainTextArea.getText();
        byte[] ciphertext = encryptCipher.doFinal(plaintext.getBytes());

        //update gui
        cipherTextArea.clear();
        cipherTextArea.setText(new String(ciphertext, StandardCharsets.UTF_8));

        log.info("" + keyPane.getCipherKey());
        log.info("Ciphertext " + new String(ciphertext));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

  class DecryptBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      if (StringUtils.isEmpty(cipherTextArea.getText())) {
        return;
      }
      try {
        //init cipher
        cipherInit();

        String ciphertext = cipherTextArea.getText();
        byte[] plaintext = decryptCipher.doFinal(ciphertext.getBytes());
        plainTextArea.clear();
        plainTextArea.setText(new String(plaintext, StandardCharsets.UTF_8));

        log.info("" + keyPane.getCipherKey());
        log.info("Plaintext " + new String(plaintext));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

  class ClearBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      CompletableFuture.runAsync(() -> {
        keyPane.removeKey();
        plainTextArea.clear();
        cipherTextArea.clear();
      });

    }
  }

  class ExportBtnPressed implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      DirectoryChooser dirChooser = new DirectoryChooser();
      dirChooser.setTitle("Export data");
      final File file = dirChooser.showDialog(stage);
      CompletableFuture.runAsync(() -> {
        storeCipherKey(keyPane.getKey(), file.getPath());
        storeInputText(plainTextArea.getText().getBytes(), file.getPath());
        storeOutputText(cipherTextArea.getText().getBytes(), file.getPath());
      });
    }

    private void storeCipherKey(Key key, String filePath) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, "rh_cipher.key"))) {
        os.write(key.getEncoded());
        os.flush();
      } catch (Exception e) {
        //do nothing yet
      }
    }

    private void storeInputText(byte[] inputContent, String filePath) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, "rh_plaintext"))) {
        os.write(inputContent);
        os.flush();
      } catch (Exception e) {
        //do nothing
      }
    }

    private void storeOutputText(byte[] outputContent, String filePath) {
      try (OutputStream os = FileUtils.openOutputStream(new File(filePath, "rh_ciphertext"))) {
        os.write(outputContent);
        os.flush();
      } catch (Exception e) {
        //do nothing
      }
    }
  }

}
