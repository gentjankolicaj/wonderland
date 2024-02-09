package io.wonderland.rh.digest;

import io.wonderland.rh.common.ServiceTab;
import io.wonderland.rh.common.TextPane;
import io.wonderland.rh.exception.ServiceException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DigestTab extends ServiceTab<MessageDigest> {

  private final HBox infoBox = new HBox();
  private final TextArea messageTextArea = new TextArea();
  private final TextArea digestTextArea = new TextArea();
  private MessageDigest messageDigest = getDefaultService();

  public DigestTab(Stage stage,String title, String serviceType) {
    super(stage, title, serviceType);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createDigestsPane());

    final BorderPane borderPane = new BorderPane();
    borderPane.setCenter(createDigestIOPane());

    splitPane.getItems().addAll(stackPane, borderPane);
    splitPane.setDividerPositions(0.3f, 0.6f);

    this.setContent(splitPane);
  }

  @Override
  protected MessageDigest getDefaultService() {
    MessageDigest tmp = null;
    try {
      tmp = MessageDigest.getInstance("SHA-1", "SUN");
      log.info("Default message-digest '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e1) {
      log.warn("Error finding default message-digest SHA-1 - SUN.", e1);
      log.warn("Attempting to find random one.");

      try {
        List<String> cspNames = Arrays.stream(Security.getProviders()).map(Provider::getName)
            .collect(Collectors.toList());
        String algName = null;
        for (String cspName : cspNames) {
          Optional<String> optional = Security.getProvider(cspName).getServices().stream()
              .filter(s -> Arrays.stream(serviceTypes).anyMatch(st->st.equals(s.getType())))
              .map(Service::getAlgorithm).filter(this::isValidServiceName).findFirst();
          if (optional.isPresent()) {
            algName = optional.get();
            break;
          }
        }
        assert algName != null;
        tmp = MessageDigest.getInstance(algName);
        log.info("Default message-digest '{}' - provider '{}' ", tmp.getAlgorithm(),
            tmp.getProvider().getName());
      } catch (Exception e2) {
        log.error("Failed to find random message-digest to all CSP.", e2);
      }
    }
    updateServiceInfo(tmp);
    return tmp;
  }

  @Override
  protected MessageDigest getService(String serviceName) throws ServiceException {
    try {
      MessageDigest tmp = MessageDigest.getInstance(serviceName);
      log.info("Selected message-digest '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
      return tmp;
    } catch (Exception e) {
      log.error("Failed to instantiate message-digest service '{}'", serviceName);
    }
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
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectMessageDigest(newItem.getValue()));

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

  private SplitPane createDigestIOPane() {
    SplitPane rootSplitter = new SplitPane();
    rootSplitter.setOrientation(Orientation.VERTICAL);

    BorderPane mainPane = new BorderPane();

    mainPane.setTop(getMiscBox());
    mainPane.setCenter(getMessageBox());

    rootSplitter.getItems().add(mainPane);
    rootSplitter.setDividerPositions(0.70f, 0.30f);
    return rootSplitter;
  }

  private VBox getMiscBox() {
    final VBox miscBox = new VBox();
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Digest name : " + getDigestName(this.messageDigest) + "   *** "));

    //button
    BorderPane buttonPane = getButtonPane();

    miscBox.getChildren().addAll(this.infoBox, buttonPane);
    return miscBox;
  }

  private HBox getMessageBox() {
    //Message box for plain & cipher text
    HBox messageBox = new HBox();
    messageBox.getChildren().addAll(new TextPane("Message", messageTextArea), new TextPane("Digest", digestTextArea));
    return messageBox;
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

    Button digestBtn = new Button("digest");
    digestBtn.setPrefWidth(120);
    digestBtn.setOnMousePressed(new MessageDigestBtnReleased());

    box.setSpacing(10);
    box.getChildren().addAll(digestBtn);
    return box;
  }

  private String getDigestName(MessageDigest messageDigest) {
    if (Objects.nonNull(messageDigest)) {
      int lastDot = messageDigest.getClass().getName().lastIndexOf(".");
      return messageDigest.getClass().getName().substring(lastDot + 1);
    }
    return StringUtils.EMPTY;
  }


  private void selectMessageDigest(String messageDigestName) {
    if (StringUtils.isEmpty(messageDigestName)) {
      return;
    }
    this.messageDigest = getService(messageDigestName);
    this.updateServiceInfo(messageDigest);
  }

  private void updateServiceInfo(MessageDigest messageDigest) {
    if (messageDigest != null) {
      this.infoBox.getChildren().remove(0);
      this.infoBox.getChildren().add(new Label(
          "Name : " + messageDigest.getAlgorithm() + " , length : " + messageDigest.getDigestLength()
              + "  *** Provider : " + messageDigest.getProvider().getName()));
    } else {
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      this.infoBox.getChildren().add(new Label("Name :"));
    }
  }

  class MessageDigestBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      if (StringUtils.isEmpty(messageTextArea.getText())) {
        return;
      }
      try {
        String input = messageTextArea.getText();
        byte[] inputDigested = messageDigest.digest(input.getBytes());
        log.info("Digested message '{}', digest '{}'", input, new String(inputDigested));

        //update digest text area
        digestTextArea.clear();
        digestTextArea.setText(new String(inputDigested, StandardCharsets.UTF_8));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

}
