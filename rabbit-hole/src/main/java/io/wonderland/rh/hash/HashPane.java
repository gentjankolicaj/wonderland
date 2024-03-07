package io.wonderland.rh.hash;

import io.wonderland.rh.common.TextPane;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HashPane extends BorderPane {
  private final HBox infoBox = new HBox();
  private final TextArea messageTextArea = new TextArea();
  private final TextArea digestTextArea = new TextArea();
  private Stage stage;
  private String messageDigestName;
  private Optional<MessageDigest> optionalMD;

  public HashPane(Stage stage, String messageDigestName) throws NoSuchAlgorithmException {
    this.stage=stage;
    this.messageDigestName=messageDigestName;
    this.build();
  }

  private void build() throws NoSuchAlgorithmException {
    this.optionalMD = getMessageDigestInstance(messageDigestName);
    this.setTop(getToolPanel());
    this.updateToolPanel();
  }


  private VBox getToolPanel() {
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

  private void updateToolPanel() {
    this.setCenter(getMessageBox());
    if (optionalMD.isPresent()) {
      MessageDigest messageDigest=optionalMD.get();
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

  protected Optional<MessageDigest> getMessageDigestInstance(String serviceName) throws  NoSuchAlgorithmException {
    MessageDigest tmp=MessageDigest.getInstance(serviceName);
      log.info("Selected message-digest '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    return Optional.ofNullable(tmp);
  }


  private class MessageDigestBtnReleased implements EventHandler<Event> {
    @Override
    public void handle(Event event) {
      if (optionalMD.isEmpty()) {
        return;
      }
      try {
        MessageDigest messageDigest=optionalMD.get();
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
