package io.wonderland.rh.misc;

import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.barcode.Barcode;
import io.wonderland.rh.base.codec.CodecAlg;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Getter
@Slf4j
public class BarcodePane extends BasePane<Void, VBox, Void> {

  private final CharsetTextArea messageTextArea = new CharsetTextArea();
  private final CodecTextArea codePointTextArea = new CodecTextArea();

  //Observers of message & digest arrays
  private final TypeObserver<byte[]> messageObserver = new TypeObserver<>();
  private final TypeObserver<byte[]> codePointObserver = new TypeObserver<>();
  private final CharsetDropdown<String, Charset, Void> charsetDropdown = CharsetDropdown.create(
      CharsetDropdown.func(messageTextArea, messageObserver));
  private final TextPane messagePane = new TextPane("Message", charsetDropdown, messageTextArea);
  //text panes
  private final VBox infoBox = new VBox();
  private final BorderPane codePane = new BorderPane();
  private final CodecDropdown codecDropdown = CodecDropdown.createBarcode(barcodeFunc());

  public BarcodePane() {
    this.build();
  }

  public BarcodePane(Double width, Double height) {
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

  private void updateBarcode(Node node) {
    this.codePane.setCenter(node);
  }

  /**
   * Function applied when numeral base is changed at dropdown.
   *
   * @return functional interface impl
   */
  private Function<CodecAlg<byte[], String, String, byte[]>, Void> barcodeFunc() {
    return codecAlg -> {
      Optional<byte[]> optional = getCodePointObserver().getValue();
      if (Objects.nonNull(optional) && optional.isPresent()) {
        byte[] codePoints = optional.get();

        //encode secret bytes according to current codec
        String encoded = codecAlg.encode().apply(codePoints);

        if (codecAlg instanceof Barcode) {
          Barcode barcode = (Barcode) codecAlg;
          try {
            Either<Node, Pair<Node, Node>> either = barcode.createKeyVisual(this.container,
                Either.left(encoded));
            this.updateBarcode(either.left().get());
          } catch (Exception e) {
            ExceptionDialog ed = new ExceptionDialog(e);
            ed.showAndWait();
          }
        }
      }
      return null;
    };
  }


  /**
   * Used by reflection to create new window
   *
   * @param width  of new window
   * @param height of new window
   * @return instance of this pane
   */
  @SuppressWarnings("unused")
  public BarcodePane newInstance(Double width, Double height) {
    return new BarcodePane(width, height);
  }

  private void build() {
    this.infoBox.getChildren().addAll(GuiUtils.getTitle("Barcode : "));

    this.codePane.setTop(codecDropdown);
    BorderPane.setMargin(codePane, GlobalConstants.DEFAULT_INSETS);
    HBox.setHgrow(messagePane, Priority.ALWAYS);

    this.container.setSpacing(GlobalConstants.SPACING);
    this.container.getChildren().addAll(infoBox, messagePane, codePane);
    this.messageTextArea.setOnKeyReleased(new MessageChangeKeyReleased());
  }

  @Override
  public VBox createContainer() {
    return new VBox();
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
        log.info("Barcode: message='{}'", message.getBytes(charset));
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }


}
