package io.wonderland.rh.misc;

import io.wonderland.rh.base.fx.CharsetDropdown;
import io.wonderland.rh.base.fx.CharsetTextArea;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.CodecTextArea;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.TextPane;
import io.wonderland.rh.base.fx.base.BasePane;
import io.wonderland.rh.base.observer.TypeObserver;
import io.wonderland.rh.utils.GuiUtils;
import java.nio.charset.Charset;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EncodingPane extends BasePane<Void, VBox, Void> {

  private final CharsetTextArea messageTextArea = new CharsetTextArea();
  private final CodecTextArea codePointTextArea = new CodecTextArea();

  //Observers of message & digest arrays
  private final TypeObserver<byte[]> messageObserver = new TypeObserver<>();
  private final TypeObserver<byte[]> codePointObserver = new TypeObserver<>();
  private final CharsetDropdown<String, Charset, Void> charsetDropdown = CharsetDropdown.create(
      CharsetDropdown.func(messageTextArea, messageObserver));
  //text panes
  private final TextPane messagePane = new TextPane("Message", charsetDropdown, messageTextArea);
  private final CodecDropdown codecDropdown = CodecDropdown.createCodec(
      CodecDropdown.defaultFunc(codePointObserver, codePointTextArea));
  private final TextPane codePoint = new TextPane("Code Point", codecDropdown, codePointTextArea);


  public EncodingPane() {
    this.build();
  }

  public EncodingPane(Double width, Double height) {
    sceneBuild(width, height);
  }

  @Override
  public void sceneBuild(double sceneWidth, double sceneHeight, Void... args) {
    this.build();
    Stage stage = new Stage();
    Scene scene = new Scene(container, sceneWidth, sceneHeight);
    stage.setScene(scene);
    stage.setTitle("ENCODING WINDOW : ");
    stage.show();
  }


  /**
   * Used by reflection to create new window
   *
   * @param width  of new window
   * @param height of new window
   * @return instance of this pane
   */
  @SuppressWarnings("unused")
  public EncodingPane newInstance(Double width, Double height) {
    return new EncodingPane(width, height);
  }

  private void build() {
    this.container.getChildren().addAll(getInfoBox(), getMessageBox());
    this.messageTextArea.setOnKeyReleased(new MessageChangeKeyReleased());
  }

  @Override
  public VBox createContainer() {
    return new VBox();
  }

  private VBox getInfoBox() {
    VBox infoBox = new VBox();
    infoBox.getChildren().addAll(GuiUtils.getTitle("Encodings : "));
    return infoBox;
  }

  private HBox getMessageBox() {
    //Message box for plain & cipher text
    HBox messageBox = new HBox();
    messageBox.getChildren().addAll(messagePane, codePoint);
    messageBox.setSpacing(10);
    HBox.setHgrow(messagePane, Priority.ALWAYS);
    HBox.setHgrow(codePoint, Priority.ALWAYS);
    return messageBox;
  }

  private class MessageChangeKeyReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        String message = StringUtils.isEmpty(messageTextArea.getText()) ? StringUtils.EMPTY
            : messageTextArea.getText();
        Charset charset = charsetDropdown.getSelectedDropdownElement().getInput();
        codePointObserver.update(message.getBytes(charset));

        //update codepoint text area
        codecDropdown.applyFunc();
        log.info("Encoding: message='{}'", message.getBytes(charset));
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }


}
