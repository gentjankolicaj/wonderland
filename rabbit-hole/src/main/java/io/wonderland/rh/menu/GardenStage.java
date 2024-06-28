package io.wonderland.rh.menu;

import static io.wonderland.rh.GlobalConstants.NEW_CONN_WINDOW_SIZE;

import io.wonderland.base.JSONUtils;
import io.wonderland.garden.api.GardenClient;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.api.Clients;
import io.wonderland.rh.base.fx.ConfirmationDialog;
import io.wonderland.rh.base.fx.base.BaseStage;
import io.wonderland.rh.utils.GuiUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class GardenStage extends BaseStage {

  public static final String TITLE = "Connect to garden...";

  GardenStage() {
    super(TITLE, false);
    this.setScene(
        new Scene(new NewConnectionPane(), NEW_CONN_WINDOW_SIZE[0], NEW_CONN_WINDOW_SIZE[1]));
  }

  class NewConnectionPane extends BorderPane {

    private static final String DEFAULT_SERVER_NAME = "garden";
    private static final String DEFAULT_CONN_NAME = "connection";
    private static final String DEFAULT_HOST_NAME = "127.0.0.1";
    private static final String DEFAULT_PORT = "9001";
    private static final String DEFAULT_USERNAME = "";

    private final BorderPane centerContainer = new BorderPane();
    private final HBox bottomContainer = new HBox();

    //
    private final Label connNameLbl = GuiUtils.getTitle("Connection name:");
    private final TextField connNameTF = new TextField(DEFAULT_CONN_NAME);
    private final Label hostLbl = GuiUtils.getTitle("Host:");
    private final TextField hostTF = new TextField(DEFAULT_HOST_NAME);
    private final Label portLbl = GuiUtils.getTitle("Port:");
    private final TextField portTF = new TextField(DEFAULT_PORT);
    private final Label usernameLbl = GuiUtils.getTitle("Username:");
    private final TextField usernameTF = new TextField(DEFAULT_USERNAME);
    private final Label passwordLbl = GuiUtils.getTitle("Password:");
    private final PasswordField passwordTF = new PasswordField();
    private final Button connectBtn = new Button("Connect");
    private final Button okBtn = new Button("OK");
    private final Button cancelBtn = new Button("Cancel");


    NewConnectionPane() {
      this.setCenter(centerContainer);
      this.setBottom(bottomContainer);
      this.build();
      BorderPane.setMargin(centerContainer, GlobalConstants.DEFAULT_INSETS);
      BorderPane.setMargin(this, GlobalConstants.DEFAULT_INSETS);
    }

    private void build() {
      this.buildCenter();
      this.buildBottom();
    }

    private void buildCenter() {
      VBox vBox = new VBox();
      vBox.setSpacing(GlobalConstants.SPACING);
      HBox.setHgrow(connNameTF, Priority.ALWAYS);
      HBox.setHgrow(hostTF, Priority.ALWAYS);
      HBox.setHgrow(portTF, Priority.ALWAYS);
      HBox.setHgrow(usernameTF, Priority.ALWAYS);
      HBox.setHgrow(passwordTF, Priority.ALWAYS);
      vBox.getChildren().add(new HBox(GlobalConstants.SPACING, connNameLbl, connNameTF));
      vBox.getChildren().add(new HBox(GlobalConstants.SPACING, hostLbl, hostTF));
      vBox.getChildren().add(new HBox(GlobalConstants.SPACING, portLbl, portTF));
      vBox.getChildren().add(new HBox(GlobalConstants.SPACING, usernameLbl, usernameTF));
      vBox.getChildren().add(new HBox(GlobalConstants.SPACING, passwordLbl, passwordTF));
      this.centerContainer.setCenter(vBox);
    }

    private void buildBottom() {
      this.connectBtn.setOnAction(event -> connect());
      this.cancelBtn.setOnAction(event -> cancel());
      this.okBtn.setOnAction(event -> ok());
      this.bottomContainer.setSpacing(GlobalConstants.SPACING);
      this.bottomContainer.setAlignment(Pos.BOTTOM_RIGHT);
      this.bottomContainer.getChildren().addAll(connectBtn, cancelBtn, okBtn);
    }

    private void ok() {
      try {
        ConnectionParams params = new ConnectionParams(DEFAULT_SERVER_NAME, connNameTF.getText(),
            hostTF.getText(),
            portTF.getText(), usernameTF.getText(), passwordTF.getText());
        Files.createDirectories(Path.of(ServersMenu.SERVER_DIR));
        Path path = getFilePath();
        JSONUtils.write(path, params);
        ConfirmationDialog cd = new ConfirmationDialog("Confirmation",
            "Connection parameters saved.");
        cd.show();
        GardenStage.this.close();
      } catch (Exception e) {
        log.error("", e);
      }
    }

    private Path getFilePath() {
      String filename =
          DEFAULT_SERVER_NAME + "_conn_" + LocalDateTime.now()
              .format(DateTimeFormatter.ISO_DATE_TIME) + ".json";
      return Path.of(ServersMenu.SERVER_DIR, filename);
    }

    private void cancel() {
      GardenStage.this.close();
    }

    private void connect() {
      try {
        ConnectionParams params = new ConnectionParams(DEFAULT_SERVER_NAME, connNameTF.getText(),
            hostTF.getText(),
            portTF.getText(), usernameTF.getText(), passwordTF.getText());
        Optional<GardenClient> optional = Clients.createGardenClient(params);
        if (optional.isPresent()) {
          ConfirmationDialog cd = new ConfirmationDialog("Confirmation",
              "Successful connection to garden.");
          cd.showAndWait();
        }
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

}
