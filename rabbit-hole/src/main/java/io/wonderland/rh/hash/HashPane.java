package io.wonderland.rh.hash;

import io.wonderland.rh.base.common.TextPane;
import io.wonderland.rh.utils.GuiUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HashPane extends BorderPane {
  private final VBox miscBox = new VBox();
  private final TextArea messageTextArea = new TextArea();
  private final TextArea digestTextArea = new TextArea();
  private final TextPane messagePane=new TextPane("Message", messageTextArea);
  private final TextPane digestPane=new TextPane("Digest",digestTextArea);
  private final HBox messageBox=new HBox();
  private final Stage stage;
  private final Optional<MessageDigest> optionalMD;

  public HashPane(Stage stage, String messageDigestName) throws NoSuchAlgorithmException {
    this.stage=stage;
    this.optionalMD = getMessageDigestInstance(messageDigestName);
    this.build();
  }

  public HashPane( String messageDigestName,double width,double height) throws NoSuchAlgorithmException {
    this.stage=new Stage();
    Scene scene=new Scene(this,width,height);
    this.optionalMD = getMessageDigestInstance(messageDigestName);
    this.build();
    this.stage.setScene(scene);
    this.stage.setTitle("HASH WINDOW : "+messageDigestName);
    this.stage.show();
  }

  private void build() {
    this.buildToolPanel();
    this.setTop(this.miscBox);
    this.setCenter(getMessageBox());
  }

  private void buildToolPanel() {
    miscBox.setPadding(new Insets(5,5,5,5));
    miscBox.setSpacing(10);

    VBox infoBox=getInfoBox();
    //button
    BorderPane buttonPane = getButtonPane();

    miscBox.getChildren().addAll(infoBox, buttonPane);
  }

  private VBox getInfoBox(){
    VBox infoBox=new VBox();
    if(optionalMD.isPresent()) {
      MessageDigest md=optionalMD.get();
      HBox hashBox = new HBox(GuiUtils.getTitle("Hash : "),new Label(md.getAlgorithm()) );
      HBox digestLengthBox = new HBox(GuiUtils.getTitle("Digest length : "), new Label(md.getDigestLength()+" bits."));
      HBox providerBox = new HBox(
          GuiUtils.getTitle("CSP : "), new Label(md.getProvider().getName() + "-" + md.getProvider().getVersionStr()));
      HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "), new Label(md.getProvider().getInfo()));
      infoBox.getChildren().addAll(hashBox, digestLengthBox, providerBox, otherInfoBox);
    }
    return infoBox;
  }

  private HBox getMessageBox() {
    //Message box for plain & cipher text
    messageBox.getChildren().addAll(messagePane,digestPane);
    messageBox.setSpacing(10);
    HBox.setHgrow(messagePane, Priority.ALWAYS);
    HBox.setHgrow(digestPane,Priority.ALWAYS);
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

  protected Optional<MessageDigest> getMessageDigestInstance(String serviceName) throws  NoSuchAlgorithmException {
    MessageDigest tmp=MessageDigest.getInstance(serviceName);
      log.info("Selected message-digest '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    return Optional.of(tmp);
  }


  class MessageDigestBtnReleased implements EventHandler<Event> {
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
