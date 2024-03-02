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
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DigestTab extends ServiceTab<MessageDigest> {

  private final HBox infoBox = new HBox();
  private final TextArea messageTextArea = new TextArea();
  private final TextArea digestTextArea = new TextArea();
  private Optional<MessageDigest> optionalMessageDigest =Optional.empty();

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

  protected Optional<MessageDigest> getSelectedMessageDigest(String serviceName) throws ServiceException {
    MessageDigest tmp=null;
    try {
       tmp = MessageDigest.getInstance(serviceName);
      log.info("Selected message-digest '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
    } catch (Exception e) {
      log.error("Failed to instantiate message-digest service '{}'", serviceName);
    }
    return Optional.ofNullable(tmp);
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
    mainPane.setPadding(new Insets(10,10,10,10));

    mainPane.setTop(getMiscBox());
    mainPane.setCenter(getMessageBox());

    rootSplitter.getItems().add(mainPane);
    rootSplitter.setDividerPositions(0.70f, 0.30f);
    return rootSplitter;
  }

  private VBox getMiscBox() {
    final VBox miscBox = new VBox();
    miscBox.setPadding(new Insets(5,5,5,5));
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(new Label("Hash algorithm: ?"));

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

    //buttons
    VBox buttonBox = getButtonsBox();

    pane.setCenter(buttonBox);
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



  private void selectMessageDigest(String messageDigestName) {
    if (StringUtils.isEmpty(messageDigestName)) {
      return;
    }
    this.optionalMessageDigest = getSelectedMessageDigest(messageDigestName);
    this.updateServiceView();
  }

  private void updateServiceView() {
    if (optionalMessageDigest.isPresent()) {
      MessageDigest messageDigest=optionalMessageDigest.get();
      this.infoBox.getChildren().remove(0);
      this.infoBox.getChildren().add(new Label(
          "Hash algorithm : " + messageDigest.getAlgorithm() + " , length : " + messageDigest.getDigestLength()
              + " , CSP: " + messageDigest.getProvider().getName()));
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
      if (optionalMessageDigest.isEmpty()) {
        return;
      }
      try {
        MessageDigest messageDigest=optionalMessageDigest.get();
        String input =  StringUtils.isEmpty(messageTextArea.getText())? StringUtils.EMPTY:messageTextArea.getText();
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
