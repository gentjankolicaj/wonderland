package io.wonderland.rh.keygen;

import static io.wonderland.rh.GlobalConstants.BORDER_PANE_INSETS;
import static io.wonderland.rh.GlobalConstants.OCTET_STREAM;

import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.barcode.Barcode;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.CodecDropdownItem;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.observer.KeygenObservable;
import io.wonderland.rh.base.observer.TypeObserver;
import io.wonderland.rh.utils.CodecUtils;
import io.wonderland.rh.utils.GuiUtils;
import java.util.Objects;
import java.util.function.Function;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@Setter
@Slf4j
public class SecretKeyGeneratorPane extends BorderPane {

  private final TypeObserver<SecretKey> secretKeyObserver = new TypeObserver<>();
  private final Label keyLbl = GuiUtils.getTitle("Secret key");
  private final Label copyKeyEncodingLbl = new Label();
  private final Label copyKeyBytesLbl = new Label();
  private final TextField keyTF = new TextField();
  private final KeyGenerator keyGenerator;
  private final KeygenObservable keygenObservable;
  private SecretKey secretKey;
  private final CodecDropdown codecDropdown = CodecDropdown.createCodecWithBarcode(secretKeyFunc());

  public SecretKeyGeneratorPane(KeyGenerator keyGenerator, KeygenObservable keygenObservable) {
    this.keyGenerator = keyGenerator;
    this.keygenObservable = keygenObservable;
    this.build();
  }

  private void build() {
    this.setTop(codecDropdown);
    BorderPane.setMargin(codecDropdown, BORDER_PANE_INSETS);
    this.copyKeyEncodingLbl.setOnMouseReleased(new CopyKeyEncodingEventHandler());
    this.copyKeyEncodingLbl.setTooltip(new Tooltip("Copy key encoding"));
    this.copyKeyEncodingLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/copy-encoding/icons8-copy-24.png"));
    this.copyKeyBytesLbl.setOnMouseReleased(new CopyKeyBytesEventHandler());
    this.copyKeyBytesLbl.setTooltip(new Tooltip("Copy key bytes"));
    this.copyKeyBytesLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/copy-binary/icons8-binary-24.png"));
  }

  public void updateSecretKey(String encoded) {
    final HBox container = new HBox();
    this.keyTF.setText(encoded);
    this.keyTF.setEditable(false);
    HBox.setHgrow(keyTF, Priority.ALWAYS);
    container.getChildren().addAll(keyLbl, copyKeyEncodingLbl, copyKeyBytesLbl, keyTF);
    container.setSpacing(GlobalConstants.SPACING);
    BorderPane.setMargin(container, BORDER_PANE_INSETS);
    this.setCenter(container);
  }

  public void generate() {
    this.secretKey = keyGenerator.generateKey();
    this.keygenObservable.notifyObservers(Either.left(secretKey));
    this.secretKeyObserver.update(secretKey);
    this.applySelectedCodec();
  }

  private void applySelectedCodec() {
    CodecDropdownItem codecDropdownItem = this.codecDropdown.getSelectedDropdownItem();
    CodecAlg<byte[], String, String, byte[]> codecAlg = codecDropdownItem.getCodecAlg();
    codecDropdownItem.getFunc().apply(codecAlg);
  }

  public void updateSecretKeyBarcode(Node barcode) {
    final HBox skContainer = new HBox();
    skContainer.getChildren().addAll(keyLbl, copyKeyEncodingLbl, copyKeyBytesLbl);
    skContainer.setSpacing(GlobalConstants.SPACING);
    VBox.setVgrow(barcode, Priority.ALWAYS);

    VBox parentContainer = new VBox(skContainer, barcode);
    parentContainer.setSpacing(GlobalConstants.SPACING);
    setCenter(parentContainer);
    BorderPane.setMargin(parentContainer, BORDER_PANE_INSETS);
  }


  /**
   * Function applied when numeral base is changed at dropdown.
   *
   * @return functional interface impl
   */
  private Function<CodecAlg<byte[], String, String, byte[]>, Void> secretKeyFunc() {
    return codecAlg -> {
      SecretKey contextKey = getSecretKey();
      if (Objects.nonNull(contextKey) && ArrayUtils.isNotEmpty(contextKey.getEncoded())) {

        //encode secret bytes according to current codec
        String encoded = codecAlg.encode().apply(contextKey.getEncoded());

        if (codecAlg instanceof Barcode) {
          Barcode barcode = (Barcode) codecAlg;
          try {
            Either<Node, Pair<Node, Node>> either = barcode.createKeyVisual(this,
                Either.left(encoded));
            this.updateSecretKeyBarcode(either.left().get());
          } catch (Exception e) {
            ExceptionDialog ed = new ExceptionDialog(e);
            ed.showAndWait();
          }
        } else {
          //update pane with encoded
          this.updateSecretKey(encoded);
        }
      }
      return null;
    };
  }


  private class CopyKeyEncodingEventHandler implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT, CodecUtils.encodeBase10(secretKey.getEncoded(), ' '));
        Clipboard.getSystemClipboard().setContent(content);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }

  private class CopyKeyBytesEventHandler implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        ClipboardContent content = new ClipboardContent();
        content.put(OCTET_STREAM, secretKey.getEncoded());
        Clipboard.getSystemClipboard().setContent(content);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }

}

