package io.wonderland.rh.hash;

import io.wonderland.rh.base.fx.CharsetDropdown;
import io.wonderland.rh.base.fx.CharsetTextArea;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.CodecTextArea;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.TextPane;
import io.wonderland.rh.base.fx.base.BaseBorderPane;
import io.wonderland.rh.base.observer.TypeObserver;
import io.wonderland.rh.utils.GuiUtils;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HashPane extends BaseBorderPane {

  private final VBox miscBox = new VBox();
  private final CharsetTextArea messageTextArea = new CharsetTextArea();
  private final CodecTextArea digestTextArea = new CodecTextArea();

  //Observers of message & digest arrays
  private final TypeObserver<byte[]> messageObserver = new TypeObserver<>();
  private final TypeObserver<byte[]> digestObserver = new TypeObserver<>();

  //dropdowns
  private final CharsetDropdown<String, Charset, Void> charsetDropdown = CharsetDropdown.create(
      CharsetDropdown.func(messageTextArea, messageObserver));
  //text panes
  private final TextPane messagePane = new TextPane("Message", charsetDropdown, messageTextArea);
  private final CodecDropdown codecDropdown = CodecDropdown.createCodec(
      CodecDropdown.defaultFunc(digestObserver, digestTextArea));
  private final TextPane digestPane = new TextPane("Digest", codecDropdown, digestTextArea);
  private final HBox messageBox = new HBox();
  private final Optional<MessageDigest> optionalMD;

  public HashPane(String cspName, String messageDigestName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this.optionalMD = getMessageDigestInstance(cspName, messageDigestName);
    this.build();
  }

  public HashPane(String cspName, String messageDigestName, double width, double height)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    Stage stage = new Stage();
    Scene scene = new Scene(this, width, height);
    this.optionalMD = getMessageDigestInstance(cspName, messageDigestName);
    this.build();
    stage.setScene(scene);
    stage.setTitle("HASH WINDOW : " + messageDigestName);
    stage.show();
  }

  private void build() {
    this.buildToolPanel();
    this.setTop(this.miscBox);
    this.setCenter(getMessageBox());
  }

  private void buildToolPanel() {
    miscBox.setPadding(new Insets(5, 5, 5, 5));
    miscBox.setSpacing(10);

    VBox infoBox = getInfoBox();
    //button
    BorderPane buttonPane = getButtonPane();

    miscBox.getChildren().addAll(infoBox, buttonPane);
  }

  private VBox getInfoBox() {
    VBox infoBox = new VBox();
    if (optionalMD.isPresent()) {
      MessageDigest md = optionalMD.get();
      HBox hashBox = new HBox(GuiUtils.getTitle("Hash : "), new Label(md.getAlgorithm()));
      HBox digestLengthBox = new HBox(GuiUtils.getTitle("Digest length : "),
          new Label(md.getDigestLength() + " bits."));
      HBox providerBox = new HBox(
          GuiUtils.getTitle("CSP : "),
          new Label(md.getProvider().getName() + "-" + md.getProvider().getVersionStr()));
      HBox otherInfoBox = new HBox(GuiUtils.getTitle("Info : "),
          new Label(md.getProvider().getInfo()));
      infoBox.getChildren().addAll(hashBox, digestLengthBox, providerBox, otherInfoBox);
    }
    return infoBox;
  }

  private HBox getMessageBox() {
    //Message box for plain & cipher text
    messageBox.getChildren().addAll(messagePane, digestPane);
    messageBox.setSpacing(10);
    HBox.setHgrow(messagePane, Priority.ALWAYS);
    HBox.setHgrow(digestPane, Priority.ALWAYS);
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

  protected Optional<MessageDigest> getMessageDigestInstance(String cspName, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    MessageDigest tmp = MessageDigest.getInstance(serviceName, cspName);
    log.info("Selected message-digest '{}' - provider '{}' ", tmp.getAlgorithm(),
        tmp.getProvider().getName());
    return Optional.of(tmp);
  }


  private class MessageDigestBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      if (optionalMD.isEmpty()) {
        return;
      }
      try {
        MessageDigest messageDigest = optionalMD.get();
        String message = StringUtils.isEmpty(messageTextArea.getText()) ? StringUtils.EMPTY
            : messageTextArea.getText();
        byte[] digest = messageDigest.digest(message.getBytes(messageTextArea.getCharset()));
        digestObserver.update(digest);

        //update digest text area
        codecDropdown.applyFunc();
        log.info("Hash: message='{}', digest={}", message.getBytes(), digest);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }

}
