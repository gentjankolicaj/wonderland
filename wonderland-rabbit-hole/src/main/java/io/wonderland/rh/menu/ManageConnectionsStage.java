package io.wonderland.rh.menu;

import static io.wonderland.rh.GlobalConstants.MANAGE_CONN_WINDOW_SIZE;

import io.wonderland.base.JSONUtils;
import io.wonderland.garden.api.GardenClient;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.api.Clients;
import io.wonderland.rh.base.fx.ConfirmationDialog;
import io.wonderland.rh.base.fx.ConnListViewCellFactory;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.base.BaseStage;
import io.wonderland.rh.utils.GuiUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManageConnectionsStage extends BaseStage {

  public static final String TITLE = "Manage connections";

  public ManageConnectionsStage() {
    super(TITLE, false);
    this.setScene(new Scene(new ManageConnectionPane(), MANAGE_CONN_WINDOW_SIZE[0],
        MANAGE_CONN_WINDOW_SIZE[1]));
  }


  class ManageConnectionPane extends BorderPane {

    private static final String DEFAULT_CONN_NAME = "connection";
    private static final String DEFAULT_HOST_NAME = "127.0.0.1";
    private static final int DEFAULT_PORT = 9001;
    private static final String DEFAULT_USERNAME = "";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_CLIENT_TYPE = "garden";

    private final BorderPane leftContainer = new BorderPane();
    private final SplitPane splitContainer = new SplitPane();
    private final HBox bottomContainer = new HBox();

    private final Button deleteBtn = new Button("Delete");
    private final Button saveBtn = new Button("Save");
    private final Button connectBtn = new Button("Connect");
    private final Button closeBtn = new Button("Close");
    private final ListView<Path> listView = new ListView<>();
    private final ConnListViewCellFactory connListViewCellFactory = new ConnListViewCellFactory();

    ManageConnectionPane() {
      this.splitContainer.getItems()
          .addAll(leftContainer, new ConnectionContainer(DEFAULT_CLIENT_TYPE,
              DEFAULT_CONN_NAME, DEFAULT_HOST_NAME, DEFAULT_PORT, DEFAULT_USERNAME,
              DEFAULT_PASSWORD));
      this.splitContainer.setDividerPositions(0.4f, 0.6f);
      this.setCenter(splitContainer);
      this.setBottom(bottomContainer);
      this.build();
    }

    private void build() {
      this.buildLeft();
      this.buildBottom();
    }


    private void buildLeft() {
      try {
        List<Path> files = getConnectionFilePaths();
        ObservableList<Path> observableList = FXCollections.observableList(files);
        this.listView.setCellFactory(connListViewCellFactory);
        this.listView.getItems().addAll(observableList);
        this.listView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> updateSplitContainer(newValue));
        this.leftContainer.setTop(getListLabel());
        this.leftContainer.setCenter(listView);
      } catch (Exception e) {
        log.error("", e);
      }
    }

    private HBox getListLabel() {
      HBox hBox = new HBox(GuiUtils.getTitle("Connections"));
      hBox.setAlignment(Pos.CENTER);
      return hBox;
    }

    private void updateSplitContainer(Path path) {
      try {
        if (path != null) {
          ConnectionContainer container = new ConnectionContainer(path);
          this.splitContainer.getItems().remove(1);
          this.splitContainer.getItems().add(container);
          this.splitContainer.setDividerPositions(0.4f, 0.6f);
        }
      } catch (IOException e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }

    private List<Path> getConnectionFilePaths() throws IOException {
      Path path = Path.of(ServersMenu.SERVER_DIR);
      try (Stream<Path> stream = Files.walk(path)) {
        return stream.filter(p -> p.toFile().isFile()).collect(Collectors.toList());
      }
    }

    private void buildBottom() {
      this.deleteBtn.setOnAction(event -> delete());
      this.saveBtn.setOnAction(event -> save());
      this.connectBtn.setOnAction(event -> connect());
      this.closeBtn.setOnAction(event -> close());
      this.bottomContainer.setAlignment(Pos.BOTTOM_RIGHT);
      this.bottomContainer.setSpacing(GlobalConstants.SPACING);
      this.bottomContainer.getChildren().addAll(deleteBtn, saveBtn, connectBtn, closeBtn);
    }

    private void connect() {
      try {
        Path path = listView.getSelectionModel().getSelectedItem();
        ConnectionParams params = JSONUtils.read(path, ConnectionParams.class);
        Optional<GardenClient> optional = Clients.createGardenClient(params);
        if (optional.isPresent()) {
          //todo: update connection icon
          ConfirmationDialog cd = new ConfirmationDialog("Connection confirmation",
              "Successful connection to garden.");
          cd.show();
        }
      } catch (Exception e) {
        log.error("", e);
      }
    }

    private void close() {
      ManageConnectionsStage.this.close();
    }

    private void delete() {
      Path path = listView.getSelectionModel().getSelectedItem();
      if (path != null) {
        try {
          Files.delete(path);
          listView.getItems().remove(path);
        } catch (IOException e) {
          log.error("", e);
        }
      }
    }

    private void save() {
      try {
        Path path = listView.getSelectionModel().getSelectedItem();
        Node node = splitContainer.getItems().get(1);
        if (node != null && path != null) {
          ConnectionContainer container = (ConnectionContainer) node;
          ConnectionParams params = container.getConnectionParams();
          JSONUtils.write(path, params);
          new ConfirmationDialog("Success", "Connection changes saved.").show();
        }
      } catch (IOException ioe) {
        ExceptionDialog ed = new ExceptionDialog(ioe);
        ed.showAndWait();
      }
    }


    class ConnectionContainer extends BorderPane {

      private final Label clientTypeLbl = GuiUtils.getTitle("Client type:");
      private final TextField clientTypeTF = new TextField("");
      private final Label connNameLbl = GuiUtils.getTitle("Connection name:");
      private final TextField connNameTF = new TextField(DEFAULT_CONN_NAME);
      private final Label hostLbl = GuiUtils.getTitle("Host:");
      private final TextField hostTF = new TextField(DEFAULT_HOST_NAME);
      private final Label portLbl = GuiUtils.getTitle("Port:");
      private final TextField portTF = new TextField("" + DEFAULT_PORT);
      private final Label usernameLbl = GuiUtils.getTitle("Username:");
      private final TextField usernameTF = new TextField(DEFAULT_USERNAME);
      private final Label passwordLbl = GuiUtils.getTitle("Password:");
      private final PasswordField passwordTF = new PasswordField();

      ConnectionContainer(Path path) throws IOException {
        build(path);
      }

      ConnectionContainer(String clientType, String connName, String host, int port,
          String username, String password) {
        build(clientType, connName, host, port, username, password);
      }

      private void build(Path path) throws IOException {
        ConnectionParams params = JSONUtils.read(path, ConnectionParams.class);
        build(params.getClientType(), params.getConnectionName(), params.getHost(),
            params.getPort(),
            params.getUsername(), params.getPassword());
      }

      private void build(String clientType, String connName, String host, int port, String username,
          String password) {
        this.clientTypeTF.setText(clientType);
        this.connNameTF.setText(connName);
        this.hostTF.setText(host);
        this.portTF.setText("" + port);
        this.usernameTF.setText(username);
        this.passwordTF.setText(password);
        VBox vBox = new VBox();
        vBox.setSpacing(GlobalConstants.SPACING);
        HBox.setHgrow(clientTypeTF, Priority.ALWAYS);
        HBox.setHgrow(connNameTF, Priority.ALWAYS);
        HBox.setHgrow(hostTF, Priority.ALWAYS);
        HBox.setHgrow(portTF, Priority.ALWAYS);
        HBox.setHgrow(usernameTF, Priority.ALWAYS);
        HBox.setHgrow(passwordTF, Priority.ALWAYS);
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, clientTypeLbl, clientTypeTF));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, connNameLbl, connNameTF));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, hostLbl, hostTF));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, portLbl, portTF));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, usernameLbl, usernameTF));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, passwordLbl, passwordTF));
        this.setCenter(vBox);
        BorderPane.setMargin(this, GlobalConstants.DEFAULT_INSETS);
      }

      public ConnectionParams getConnectionParams() {
        return new ConnectionParams(clientTypeTF.getText(), connNameTF.getText(), hostTF.getText(),
            portTF.getText(),
            usernameTF.getText()
            , passwordTF.getText());
      }
    }


  }

}
